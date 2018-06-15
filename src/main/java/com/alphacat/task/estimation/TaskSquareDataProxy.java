package com.alphacat.task.estimation;

import com.alphacat.mapper.*;
import com.alphacat.pojo.SquareTag;
import com.alphacat.pojo.Task;
import com.alphacat.tag.square.SquareTagConverter;
import com.alphacat.vo.SquareVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TaskSquareDataProxy {

    private PictureMapper pictureMapper;
    private SquareTagMapper squareTagMapper;
    private SquareTagConverter tagConverter;
    private WorkerMapper workerMapper;
    private LabelMapper labelMapper;
    private TaskRecordMapper recordMapper;
    private TaskMapper taskMapper;
    private SquareClusterer clusterer;

    @Autowired
    public TaskSquareDataProxy(PictureMapper pictureMapper, SquareTagMapper squareTagMapper,
                               SquareTagConverter tagConverter, WorkerMapper workerMapper,
                               LabelMapper labelMapper, TaskRecordMapper recordMapper,
                               TaskMapper taskMapper, SquareClusterer clusterer) {
        this.pictureMapper = pictureMapper;
        this.squareTagMapper = squareTagMapper;
        this.tagConverter = tagConverter;
        this.workerMapper = workerMapper;
        this.labelMapper = labelMapper;
        this.recordMapper = recordMapper;
        this.taskMapper = taskMapper;
        this.clusterer = clusterer;
    }

    public TaskSquareData createTaskData(int id) {
        Task task = taskMapper.get(id);
        switch (task.getMethod()) {
            case 1: return initSingleSquareTask(id);
            case 2: return initMultiSquareTask(id);
            case 3: // TODO init irregular tag task data
            default: return null;
        }
    }

    public void storeTaskData(TaskSquareData taskSquareData) {
        // TODO add new gold answers.
        taskSquareData.getWorkerMap().forEach((id, w) -> {
            workerMapper.updateAccuracy(id, w.getFinalRectAccuracy(), w.getFinalLabelAccuracy());
            recordMapper.setAccuracy(taskSquareData.getId(), id, w.getRectAccuracy(), w.getLabelAccuracy());
        });
    }

    private TaskSquareData initSingleSquareTask(int id) {
        final int rid = 0;
        // init pictureMap
        List<Integer> pictures = pictureMapper.get(id);
        Map<SquarePictureKey, PictureData> pictureMap = new HashMap<>(pictures.size());
        pictures.forEach(p -> pictureMap.put(new SquarePictureKey(p, rid),
                new PictureData(squareTagMapper.getWorkersFromSquares(id, p))));
        // init workerMap
        List<Integer> workerIds = squareTagMapper.getWorkers(id);
        Map<Integer, WorkerSquareData> workers = new HashMap<>();
        workerIds.forEach(w -> {
            List<Integer> pics = squareTagMapper.getPicsByTaskAndWorker(id, w);
            Map<SquarePictureKey, SquareVO> answers = new HashMap<>(pics.size());
            pics.forEach(p -> answers.put(new SquarePictureKey(p, rid),
                    tagConverter.toVO(squareTagMapper.get(w, id, p).get(0))));
            double historyRectAccuracy = workerMapper.getRectAccuracy(w);
            double historyLabelAccuracy = workerMapper.getLabelAccuracy(w);
            long historyPicNum = squareTagMapper.getHistoryPicNum(w);
            workers.put(w, new WorkerSquareData(answers, historyRectAccuracy, historyLabelAccuracy, historyPicNum));
        });
        int labelNum = labelMapper.get(id).size();
        return new TaskSquareData(id, labelNum, pictureMap, workers);
    }

    private TaskSquareData initMultiSquareTask(int id) {
        Map<Integer, List<List<SquareTag>>> tags = clusterer.kMeans(squareTagMapper.getByTask(id));
        // init workers
        Map<Integer, WorkerSquareData> workers = new HashMap<>();
        tags.forEach((pid, clusters) -> {
            for(int i = 0; i < clusters.size(); i++) {
                SquarePictureKey picKey = new SquarePictureKey(pid, i);
                clusters.get(i).forEach(t -> {
                    int wid = t.getWorkerId();
                    SquareVO s = tagConverter.toVO(t);
                    if(workers.containsKey(wid)) {
                        workers.get(wid).getAnswers().put(picKey, s);
                    } else {
                        Map<SquarePictureKey, SquareVO> answers = new HashMap<>();
                        answers.put(picKey, s);
                        double rectAccuracy = workerMapper.getRectAccuracy(wid);
                        double labelAccuracy = workerMapper.getLabelAccuracy(wid);
                        long historyNum = squareTagMapper.getHistoryPicNum(wid);
                        workers.put(wid, new WorkerSquareData(answers, rectAccuracy, labelAccuracy, historyNum));
                    }
                });
            }
        });
        // init pictures
        Map<SquarePictureKey, PictureData> pictures = new HashMap<>();
        tags.forEach((pid, clusters) -> {
            for(int i = 0; i < clusters.size(); i++) {
                pictures.put(new SquarePictureKey(pid, i), new PictureData(clusters.get(i).stream()
                        .map(SquareTag::getWorkerId).collect(Collectors.toList())));
            }
        });
        int labelNum = labelMapper.get(id).size();
        return new TaskSquareData(id, labelNum, pictures, workers);
    }

}
