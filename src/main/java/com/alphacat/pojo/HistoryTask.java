package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * A history task which a worker has done before.
 * It is a type of worker's task.
 * @see AvailableTask the other type
 * @see Task the original type or the database type
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class HistoryTask {

    private int id;
    private String name;
    private Date endTime;
    private int earnedCredit;
    /**
     * Accuracy of a worker's rectangles in this task.
     * Value ranges from 0 to 1.
     */
    private double rectAccuracy;
    /**
     * Accuracy of a worker's labels in this task.
     * Value ranges from 0 to 1.
     */
    private double labelAccuracy;

}
