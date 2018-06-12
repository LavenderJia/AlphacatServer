package com.alphacat.task.estimation;

import com.alphacat.vo.SquareVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Estimator {

    private SpamFilter filter;
    private TaskDataProxy taskDataProxy;

    @Autowired
    public Estimator(SpamFilter filter, TaskDataProxy proxy) {
        this.filter = filter;
        this.taskDataProxy = proxy;
    }

    /**
     * Estimate a task, update all related workers' accuracies and store the estimated answer.
     * @param id the task id
     */
    public void estimate(int id) {
        TaskData taskData = taskDataProxy.createTaskData(id);
        filter.filterSpams(taskData);
        Map<Integer, Integer[]> rects = estimateRects(taskData);
        Map<Integer, Map<String, String>> labelData = estimateLabels(taskData);
        Map<Integer, SquareVO> gold = new HashMap<>(rects.size());
        rects.forEach((p, r) -> gold.put(p, new SquareVO(0, r[0], r[1], r[2], r[3], labelData.get(p), null)));
        taskData.setGold(gold);
        taskDataProxy.storeTaskData(taskData);
    }

    private Map<Integer, Integer[]> estimateRects(TaskData taskData) {
        int picNum = taskData.getPictureMap().size();
        Map<Integer, Integer[]> goldRects = new HashMap<>(picNum);
        boolean coveraged = false;
        taskData.getPictureMap().keySet().forEach(p -> goldRects.put(p, getMiddleRect(taskData, p)));
        while(!coveraged) {
            taskData.getWorkerMap().forEach((i, w) -> updateWorkerRectAccuracy(w, goldRects));
            coveraged = taskData.getPictureMap().keySet().stream().map(p -> {
                Integer[] rects = getMiddleRect(taskData, p);
                if(rects == null) {
                    return true;
                }
                boolean similar = rectSimilar(rects, goldRects.get(p));
                goldRects.put(p, rects);
                return similar;
            }).allMatch(s -> s);
        }
        taskData.getWorkerMap().forEach((i, w) -> updateWorkerRectAccuracy(w, goldRects));
        return goldRects;
    }

    private boolean rectSimilar(Integer[] a, Integer[] b) {
        final int d = 10;
        return Math.abs(a[0] - b[0]) <= d
                && Math.abs(a[1] - b[1]) <= d
                && Math.abs(a[2] - b[2]) <= d
                && Math.abs(a[3] - b[3]) <= d;
    }

    private Integer[] getMiddleRect(TaskData taskData, int p) {
        Map<Integer, WorkerSquareData> workerMap = taskData.getWorkerMap();
        List<Integer> workers = taskData.getPictureMap().get(p).getWorkers();
        if(workers.isEmpty()) {
            return null;
        }
        double sum = workers.stream().mapToDouble(id -> workerMap.get(id).getRectAccuracy()).sum();
        int x = (int) (workers.stream()
                .mapToDouble(id -> workerMap.get(id).getAnswers().get(p).getX() * workerMap.get(id).getRectAccuracy())
                .sum() / sum);
        int y = (int) (workers.stream()
                .mapToDouble(id -> workerMap.get(id).getAnswers().get(p).getY() * workerMap.get(id).getRectAccuracy())
                .sum() / sum);
        int w = (int) (workers.stream()
                .mapToDouble(id -> workerMap.get(id).getAnswers().get(p).getW() * workerMap.get(id).getRectAccuracy())
                .sum() / sum);
        int h = (int) (workers.stream()
                .mapToDouble(id -> workerMap.get(id).getAnswers().get(p).getH() * workerMap.get(id).getRectAccuracy())
                .sum() / sum);
        return new Integer[]{x, y, w, h};
    }

    private void updateWorkerRectAccuracy(WorkerSquareData w, Map<Integer, Integer[]> ans) {
        double rectAccuracy = w.getAnswers().entrySet().stream()
                .mapToDouble(e -> getRectCod(ans.get(e.getKey()), e.getValue()))
                .sum() / w.getNum();
        w.setRectAccuracy(rectAccuracy);
    }

    private double getRectCod(Integer[] s, SquareVO base) {
        int leftX = Math.max(s[0], base.getX()),
            rightX = Math.min(s[0] + s[2], base.getX() + base.getW()),
            upY = Math.max(s[1], base.getY()),
            downY = Math.min(s[1] + s[3], base.getY() + base.getH());
        if(leftX >= rightX || upY >= downY) {
            return 0.0;
        }
        return (rightX - leftX) * (downY - upY) / (base.getW() * base.getH() * 1.0);
    }

    private Map<Integer, Map<String, String>> estimateLabels(TaskData taskData) {
        int picNum = taskData.getPictureMap().size();
        Map<Integer, Map<String, String>> goldLabels = new HashMap<>(picNum);
        taskData.getPictureMap().keySet().forEach(p -> goldLabels.put(p, getMiddleLabels(taskData, p)));
        boolean coveraged = false;
        while(!coveraged) {
            taskData.getWorkerMap().forEach((id, w) -> updateWorkerLabelAccuracy(w, goldLabels, taskData.getLabelNum()));
            coveraged = taskData.getPictureMap().keySet().stream().map(p -> {
                Map<String, String> labelData = getMiddleLabels(taskData, p);
                if(labelData == null) {
                    return true;
                }
                boolean same = labelSame(labelData, goldLabels.get(p));
                if(same) {
                    return true;
                } else {
                    goldLabels.put(p, labelData);
                    return false;
                }
            }).allMatch(s -> s);
        }
        taskData.getWorkerMap().forEach((id, w) -> updateWorkerLabelAccuracy(w, goldLabels, taskData.getLabelNum()));
        return goldLabels;
    }

    private boolean labelSame(Map<String, String> a, Map<String, String> b) {
        return a.entrySet().stream().mapToInt(e -> e.getValue().equals(b.get(e.getKey())) ? 0 : 1).sum() == 0;
    }

    private Map<String, String> getMiddleLabels(TaskData taskData, int p) {
        int labelNum = taskData.getLabelNum();
        List<Integer> workers = taskData.getPictureMap().get(p).getWorkers();
        if(workers.isEmpty()) {
            return null;
        }
        Map<String, String> result = new HashMap<>(labelNum);
        Map<String, Map<String, Double>> data = new HashMap<>(labelNum);
        workers.forEach(id -> taskData.getWorkerMap().get(id).getAnswers().get(p).getLabelData().forEach((k, v) -> {
            double accuracy = taskData.getWorkerMap().get(id).getLabelAccuracy();
            if(data.containsKey(k)) {
                if(data.get(k).containsKey(v)) {
                    data.get(k).put(v, data.get(k).get(v) + accuracy);
                } else {
                    data.get(k).put(v, accuracy);
                }
            } else {
                Map<String, Double> values = new HashMap<>();
                values.put(v, accuracy);
                data.put(k, values);
            }
        }));
        data.forEach((k, vw) -> result.put(k, vw.entrySet().stream().max((a, b) -> {
            if(a.getValue() < b.getValue()) return -1;
            if(a.getValue() == b.getValue()) return 0;
            return 1;
        }).get().getKey()));
        return result;
    }

    private void updateWorkerLabelAccuracy(WorkerSquareData w, Map<Integer, Map<String, String>> ans, int labelNum) {
        double accuracy = w.getAnswers().entrySet().stream()
                .mapToInt(e -> ans.get(e.getKey()).entrySet().stream()
                        .mapToInt(en -> en.getValue().equals(e.getValue().getLabelData().get(en.getKey())) ? 1 : 0).sum())
                .sum() / (w.getNum() * labelNum * 1.0);
        w.setLabelAccuracy(accuracy);
    }

}
