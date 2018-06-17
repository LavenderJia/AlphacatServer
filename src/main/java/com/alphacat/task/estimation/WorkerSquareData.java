package com.alphacat.task.estimation;

import com.alphacat.vo.SquareVO;
import lombok.Data;

import java.util.Map;

@Data
public class WorkerSquareData {

    private Map<SquarePictureKey, SquareVO> answers;
    private double historyRectAccuracy;
    private double historyLabelAccuracy;
    private long historyNum;
    private double rectAccuracy;
    private double labelAccuracy;

    WorkerSquareData(Map<SquarePictureKey, SquareVO> answers, double historyRectAccuracy,
                            double historyLabelAccuracy, long historyNum) {
        this.answers = answers;
        this.rectAccuracy = this.historyRectAccuracy = historyRectAccuracy;
        this.labelAccuracy = this.historyLabelAccuracy = historyLabelAccuracy;
        this.historyNum = historyNum;
    }

    public int getNum() {
        return answers.size();
    }

    public double getFinalRectAccuracy() {
        double newWeight = this.getNum() / (this.getNum() + historyNum);
        double historyWeight = 1 - newWeight;
        return this.historyRectAccuracy * historyWeight +
                this.rectAccuracy * newWeight;
    }

    public double getFinalLabelAccuracy() {
        double newWeight = this.getNum() / (this.getNum() + historyNum);
        double historyWeight = 1 - newWeight;
        return this.historyLabelAccuracy * historyWeight +
                this.labelAccuracy * newWeight;
    }

}
