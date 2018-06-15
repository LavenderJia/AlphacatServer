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
     * Filter off rect spams and label spams from #taskSquareData.
     */
    public void filterSpams(TaskSquareData taskSquareData) {
        final double rrsThreshold = 240.0,
                rusThreshold = 90.0,
                lrsThreshold = 0.7,
                lusThreshold = taskSquareData.getPictureMap().size() > 10 ? 0.2 : 0.1;
        List<Integer> rectSpams = taskSquareData.getWorkerMap().keySet().stream()
                .filter(id -> rectRandomSpam(taskSquareData, id) > rrsThreshold
                        && rectUniformSpam(taskSquareData, id) > rusThreshold)
                .collect(Collectors.toList());
        List<Integer> labelSpams = taskSquareData.getWorkerMap().keySet().stream()
                .filter(id -> labelRandomSpam(taskSquareData, id) <= lrsThreshold
                        && labelUniformSpam(taskSquareData, id) <= lusThreshold)
                .collect(Collectors.toList());
        taskSquareData.processRectSpams(rectSpams);
        taskSquareData.processLabelSpams(labelSpams);
    }

   /**
     * @param id the worker's id
     */
    private double rectRandomSpam(TaskSquareData taskSquareData, int id) {
        double nominator, denominator;
        // calculate nominator
        WorkerSquareData w = taskSquareData.getWorkerMap().get(id);
        nominator = w.getAnswers().entrySet().stream()
                .mapToDouble(e -> taskSquareData.getPictureMap().get(e.getKey()).getWorkers().stream()
                    .mapToDouble(i -> rectDiff(e.getValue(), taskSquareData.getWorkerMap().get(i)
                            .getAnswers().get(e.getKey())))
                    .sum())
                .sum();
        // calculate denominator
        denominator = w.getAnswers().entrySet().stream()
                .mapToDouble(e -> taskSquareData.getPictureMap().get(e.getKey()).getWorkers().size() - 1)
                .sum();
        return denominator == 0.0 ? Double.NaN : nominator / denominator;
    }

    /**
     * @param id the worker's id
     */
    private double rectUniformSpam(TaskSquareData taskSquareData, int id) {
        WorkerSquareData w = taskSquareData.getWorkerMap().get(id);
        Map<SquarePictureKey, PictureData> pictures = taskSquareData.getPictureMap();
        Map<Integer, WorkerSquareData> workers = taskSquareData.getWorkerMap();
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
    private double labelRandomSpam(TaskSquareData taskSquareData, int id) {
        Map<SquarePictureKey, PictureData> pics = taskSquareData.getPictureMap();
        WorkerSquareData w = taskSquareData.getWorkerMap().get(id);
        double nominator = w.getAnswers().entrySet().stream()
                .mapToDouble(e -> labelDiff(taskSquareData, id, e.getKey()))
                .sum();
        double denominator = w.getAnswers().entrySet().stream()
                .mapToDouble(e -> pics.get(e.getKey()).getWorkers().size() - 1)
                .sum() * taskSquareData.getLabelNum();
        return denominator == 0.0 ? Double.NaN : nominator / denominator;
    }

    /**
     * @param id the worker's id
     */
    private double labelUniformSpam(TaskSquareData taskSquareData, int id) {
        Map<Integer, WorkerSquareData> workers = taskSquareData.getWorkerMap();
        Map<SquarePictureKey, PictureData> pics = taskSquareData.getPictureMap();
        WorkerSquareData w = workers.get(id);
        double nominator = w.getAnswers().entrySet().stream()
                .mapToDouble(e -> labelSelfCoincidence(taskSquareData, id, e.getKey())
                        * labelDiff(taskSquareData, id, e.getKey()))
                .sum();
        double denominator = w.getAnswers().entrySet().stream()
                .mapToDouble(e -> pics.get(e.getKey()).getWorkers().size() - 1)
                .sum() - w.getNum() - taskSquareData.getLabelNum() * taskSquareData.getLabelNum();
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
    private int labelDiff(TaskSquareData taskSquareData, int w, SquarePictureKey p) {
        Map<String, String> base = taskSquareData.getWorkerMap().get(w).getAnswers().get(p).getLabelData();
        return taskSquareData.getPictureMap().get(p).getWorkers().stream()
                .mapToInt(i -> taskSquareData.getWorkerMap().get(i).getAnswers()
                        .get(p).getLabelData().entrySet().stream()
                        .mapToInt(e -> e.getValue().equals(base.get(e.getKey())) ? 0 : 1)
                        .sum())
                .sum();
    }

    /**
     * @return the sum of label coincidence on the worker w between picture p and other pictures tagged by the worker w.
     */
    private int labelSelfCoincidence(TaskSquareData taskSquareData, int w, SquarePictureKey p) {
        Map<SquarePictureKey, SquareVO> answers = taskSquareData.getWorkerMap().get(w).getAnswers();
        Map<String, String> base = answers.get(p).getLabelData();
        return answers.entrySet().stream().mapToInt(e -> {
            Map<String, String> comparison = answers.get(e.getKey()).getLabelData();
            return base.entrySet().stream().mapToInt(en -> en.getValue().equals(comparison.get(en.getKey())) ? 1 : 0).sum();
        }).sum() - 2 * taskSquareData.getLabelNum();
    }

}
