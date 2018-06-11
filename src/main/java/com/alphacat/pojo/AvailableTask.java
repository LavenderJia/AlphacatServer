package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * An available task for a worker to do. Including
 * what a worker does or does not take part in.
 * This is a type of worker's task.
 * @see HistoryTask the other type
 * @see Task the original type, or database type
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AvailableTask {

    private int id;
    private String name;
    private int creditPerPic;
    private int creditFinished;
    private int method; // the same rule as Task
    private Date endTime;

}
