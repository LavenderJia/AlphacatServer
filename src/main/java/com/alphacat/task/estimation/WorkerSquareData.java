package com.alphacat.task.estimation;

import com.alphacat.vo.SquareVO;
import lombok.Data;

import java.util.Map;

@Data
class WorkerSquareData {

    private int num;
    private Map<Integer, SquareVO> answers;
    private double historyRectAccuracy;
    private double historyLabelAccuracy;
    private long historyNum;
    private double rectAccuracy;
    private double labelAccuracy;

    WorkerSquareData(Map<Integer, SquareVO> answers, double historyRectAccuracy,
                            double historyLabelAccuracy, long historyNum) {
        this.num = answers.size();
        this.answers = answers;
        this.rectAccuracy = this.historyRectAccuracy = historyRectAccuracy;
        this.labelAccuracy = this.historyLabelAccuracy = historyLabelAccuracy;
        this.historyNum = historyNum;
    }

    public double getFinalRectAccuracy() {
        double newWeight = num / (num + historyNum);
        double historyWeight = 1 - newWeight;
        return this.historyRectAccuracy * historyWeight +
                this.rectAccuracy * newWeight;
    }

    public double getFinalLabelAccuracy() {
        double newWeight = num / (num + historyNum);
        double historyWeight = 1 - newWeight;
        return this.historyLabelAccuracy * historyWeight +
                this.labelAccuracy * newWeight;
    }

}
