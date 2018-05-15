package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * An idle task of a requester. "Idle" means it has not started.
 * It is a type of requester's task, not about the worker.
 * @see UnderwayTask another type
 * @see EndedTask yet another type
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class IdleTask {

    private int id;
    private String name;
    private int creditPerPic;
    private int creditFinished;
    private int method;
    private Date startTime;
    private Date endTime;

}
