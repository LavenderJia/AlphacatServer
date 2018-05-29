package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * 记录积分的历史明细
 * @author 161250102
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class WorkerCredit {

    private int workerId;
    private int taskId;
    private String taskName;
    private int change;
    private Date date;
    /**
     * credit value after changed
     */
    private int credit;

}
