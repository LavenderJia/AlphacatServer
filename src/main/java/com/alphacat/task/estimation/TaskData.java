package com.alphacat.task.estimation;

import com.alphacat.vo.SquareVO;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
class TaskData {

    private int id;
    private int labelNum;
    private Map<Integer, PictureData> pictureMap;
    private Map<Integer, WorkerSquareData> workerMap;

    private Map<Integer, SquareVO> gold;
    private List<Integer> effectiveRects;
    private List<Integer> effectiveLabels;

    TaskData(int id, int labelNum, Map<Integer, PictureData> pics, Map<Integer, WorkerSquareData> workers) {
        this.id = id;
        this.labelNum = labelNum;
        this.pictureMap = pics;
        this.workerMap = workers;
    }

    public void processRectSpams(List<Integer> spams) {
        spams.forEach(s -> {
            WorkerSquareData w = workerMap.get(s);
            w.setRectAccuracy(0.0);
            w.getAnswers().keySet().forEach(p -> pictureMap.get(p).getWorkers().remove(s));
        });
        effectiveRects = workerMap.keySet().stream()
                .filter(id -> ! spams.contains(id))
                .collect(Collectors.toList());
    }

    public void processLabelSpams(List<Integer> spams) {
        spams.forEach(s -> {
            WorkerSquareData w = workerMap.get(s);
            w.setLabelAccuracy(0.0);
            w.getAnswers().keySet().forEach(p -> pictureMap.get(p).getWorkers().remove(s));
        });
        effectiveLabels = workerMap.keySet().stream()
                .filter(id -> ! spams.contains(id))
                .collect(Collectors.toList());
    }

}
