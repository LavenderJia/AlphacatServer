package com.alphacat.task.estimation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PictureData {

    /**
     * Workers who tagged this picture in this task.
     */
    private List<Integer> workers;

    public PictureData(List<Integer> workers) {
        if(workers == null) {
            this.workers = new ArrayList<>();
        } else {
            this.workers = workers;
        }
    }

}
