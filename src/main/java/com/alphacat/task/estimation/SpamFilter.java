package com.alphacat.task.estimation;

import com.alphacat.vo.SquareVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SpamFilter {

    /**
     * Filter off rect spams and label spams from #taskData.
     */
    public void filterSpams(TaskData taskData) {
        final double rrsThreshold = 240.0,
                rusThreshold = 90.0,
                lrsThreshold = 0.7,
                lusThreshold = taskData.getPictureMap().size() > 10 ? 0.2 : 0.1;
        List<Integer> rectSpams = taskData.getWorkerMap().keySet().stream()
                .filter(id -> rectRandomSpam(taskData, id) > rrsThreshold
                        && rectUniformSpam(taskData, id) > rusThreshold)
                .collect(Collectors.toList());
        List<Integer> labelSpams = taskData.getWorkerMap().keySet().stream()
                .filter(id -> labelRandomSpam(taskData, id) <= lrsThreshold
                        && labelUniformSpam(taskData, id) <= lusThreshold)
                .collect(Collectors.toList());
        taskData.processRectSpams(rectSpams);
        taskData.processLabelSpams(labelSpams);
    }

   /**
     * @param id the worker's id
     */
    private double rectRandomSpam(TaskData taskData, int id) {
        double nominator, denominator;
        // calculate nominator
        WorkerSquareData w = taskData.getWorkerMap().get(id);
        nominator = w.getAnswers().entrySet().stream()
                .mapToDouble(e -> taskData.getPictureMap().get(e.getKey()).getWorkers().stream()
                    .mapToDouble(i -> rectDiff(e.getValue(), taskData.getWorkerMap().get(i)
                            .getAnswers().get(e.getKey())))
                    .sum())
                .sum();
        // calculate denominator
        denominator = w.getAnswers().entrySet().stream()
                .mapToDouble(e -> taskData.getPictureMap().get(e.getKey()).getWorkers().size() - 1)
                .sum();
        return denominator == 0.0 ? Double.NaN : nominator / denominator;
    }

    /**
     * @param id the worker's id
     */
    private double rectUniformSpam(TaskData taskData, int id) {
        WorkerSquareData w = taskData.getWorkerMap().get(id);
        Map<Integer, PictureData> pictures = taskData.getPictureMap();
        Map<Integer, WorkerSquareData> workers = taskData.getWorkerMap();
        double nominator = w.getAnswers().entrySet().stream()
                .mapToDouble(e -> w.getAnswers().entrySet().stream()
                    .mapToDouble(en -> cosRelation(e.getValue(), en.getValue()) - 0.5)
                    .sum() * pictures.get(e.getKey()).getWorkers().stream()
                            .mapToDouble(i -> rectDiff(e.getValue(), workers.get(i).getAnswers().get(e.getKey())))
                            .sum())
                .sum();
        double denominator = w.getAnswers().entrySet().stream()
                .mapToDouble(e -> pictures.get(e.getKey()).getWorkers().size() - 1)
                .sum() * w.getNum();
        return denominator == 0.0 ? Double.NaN : nominator / denominator;
    }

    /**
     * @param id the worker's id
     */
    private double labelRandomSpam(TaskData taskData, int id) {
        Map<Integer, PictureData> pics = taskData.getPictureMap();
        WorkerSquareData w = taskData.getWorkerMap().get(id);
        double nominator = w.getAnswers().entrySet().stream()
                .mapToDouble(e -> labelDiff(taskData, id, e.getKey()))
                .sum();
        double denominator = w.getAnswers().entrySet().stream()
                .mapToDouble(e -> pics.get(e.getKey()).getWorkers().size() - 1)
                .sum() * taskData.getLabelNum();
        return denominator == 0.0 ? Double.NaN : nominator / denominator;
    }

    /**
     * @param id the worker's id
     */
    private double labelUniformSpam(TaskData taskData, int id) {
        Map<Integer, WorkerSquareData> workers = taskData.getWorkerMap();
        Map<Integer, PictureData> pics = taskData.getPictureMap();
        WorkerSquareData w = workers.get(id);
        double nominator = w.getAnswers().entrySet().stream()
                .mapToDouble(e -> labelSelfCoincidence(taskData, id, e.getKey())
                        * labelDiff(taskData, id, e.getKey()))
                .sum();
        double denominator = w.getAnswers().entrySet().stream()
                .mapToDouble(e -> pics.get(e.getKey()).getWorkers().size() - 1)
                .sum() - w.getNum() - taskData.getLabelNum() * taskData.getLabelNum();
        return denominator == 0.0 ? Double.NaN : nominator / denominator;
    }

    private double rectDiff(SquareVO a, SquareVO b) {
        Vector4D x = new Vector4D(a), y = new Vector4D(b);
        return x.diff(y).mag();
    }

    private double cosRelation(SquareVO a, SquareVO b) {
        Vector4D x = new Vector4D(a), y = new Vector4D(b);
        return x.product(y) / Math.sqrt(x.msq() * y.msq());
    }

    /**
     * @return the sum of label differences on picture p between the worker w and other workers.
     */
    private int labelDiff(TaskData taskData, int w, int p) {
        Map<String, String> base = taskData.getWorkerMap().get(w).getAnswers().get(p).getLabelData();
        return taskData.getPictureMap().get(p).getWorkers().stream()
                .mapToInt(i -> taskData.getWorkerMap().get(i).getAnswers()
                        .get(p).getLabelData().entrySet().stream()
                        .mapToInt(e -> e.getValue().equals(base.get(e.getKey())) ? 0 : 1)
                        .sum())
                .sum();
    }

    /**
     * @return the sum of label coincidence on the worker w between picture p and other pictures tagged by the worker w.
     */
    private int labelSelfCoincidence(TaskData taskData, int w, int p) {
        Map<Integer, SquareVO> answers = taskData.getWorkerMap().get(w).getAnswers();
        Map<String, String> base = answers.get(p).getLabelData();
        return answers.entrySet().stream().mapToInt(e -> {
            Map<String, String> comparison = answers.get(e.getKey()).getLabelData();
            return base.entrySet().stream().mapToInt(en -> en.getValue().equals(comparison.get(en.getKey())) ? 1 : 0).sum();
        }).sum() - 2 * taskData.getLabelNum();
    }

    @Data
    @AllArgsConstructor
    private class Vector4D {
        int a, b, c, d;
        Vector4D(SquareVO s) {
            this(s.getX(), s.getY(), s.getW(), s.getH());
        }
        Vector4D diff(Vector4D v) {
            return new Vector4D(a - v.a, b - v.b, c - v.c, d - v.d);
        }
        double product(Vector4D v) {
            return a * v.a + b * v.b + c * v.c + d * v.d;
        }
        double msq() {
            return this.product(this);
        }
        double mag() {
            return Math.sqrt(this.msq());
        }
    }

}
