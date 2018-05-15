package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * An underway task of a requester.
 * It is a type of requester's task, not about the worker.
 * @see IdleTask another type
 * @see EndedTask yet another type
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UnderwayTask {

    private int id;
    private String name;
    private Date startTime;
    private Date endTime;
    private int workerCount;
    private Double tagRate;

}
