package com.alphacat.task.estimation;

import com.alphacat.mapper.*;
import com.alphacat.pojo.Worker;
import com.alphacat.tag.square.SquareTagConverter;
import com.alphacat.vo.SquareVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TaskDataProxy {

    private PictureMapper pictureMapper;
    private SquareTagMapper squareTagMapper;
    private SquareTagConverter tagConverter;
    private WorkerMapper workerMapper;
    private LabelMapper labelMapper;
    private TaskRecordMapper recordMapper;

    @Autowired
    public TaskDataProxy(PictureMapper pictureMapper, SquareTagMapper squareTagMapper,
                         SquareTagConverter tagConverter, WorkerMapper workerMapper,
                         LabelMapper labelMapper, TaskRecordMapper recordMapper) {
        this.pictureMapper = pictureMapper;
        this.squareTagMapper = squareTagMapper;
        this.tagConverter = tagConverter;
        this.workerMapper = workerMapper;
        this.labelMapper = labelMapper;
        this.recordMapper = recordMapper;
    }

    public TaskData createTaskData(int id) {
        int labelNum = labelMapper.get(id).size();
        return new TaskData(id, labelNum, initPictures(id), initWorkers(id));
    }

    public void storeTaskData(TaskData taskData) {
        // TODO add new gold answers.
        taskData.getWorkerMap().forEach((id, w) -> {
            workerMapper.updateAccuracy(id, w.getFinalRectAccuracy(), w.getFinalLabelAccuracy());
            recordMapper.setAccuracy(taskData.getId(), id, w.getRectAccuracy(), w.getLabelAccuracy());
        });
    }

    private Map<Integer, PictureData> initPictures(int id) {
        List<Integer> pics = pictureMapper.get(id);
        Map<Integer, PictureData> pictureMap = new HashMap<>(pics.size());
        pics.forEach(p -> pictureMap.put(p, new PictureData(squareTagMapper.getWorkersFromSquares(id, p))));
        return pictureMap;
    }

    private Map<Integer, WorkerSquareData> initWorkers(int id) {
        List<Integer> workerIds = squareTagMapper.getWorkers(id);
        Map<Integer, WorkerSquareData> workers = new HashMap<>();
        workerIds.forEach(w -> {
            List<Integer> pics = squareTagMapper.getPicsByTaskAndWorker(id, w);
            Map<Integer, SquareVO> answers = new HashMap<>(pics.size());
            pics.forEach(p -> answers.put(p, tagConverter.toVO(squareTagMapper.get(w, id, p).get(0))));
            double historyRectAccuracy = workerMapper.getRectAccuracy(w);
            double historyLabelAccuracy = workerMapper.getLabelAccuracy(w);
            long historyPicNum = squareTagMapper.getHistoryPicNum(w);
            workers.put(w, new WorkerSquareData(answers, historyRectAccuracy, historyLabelAccuracy, historyPicNum));
        });
        return workers;
    }

}
