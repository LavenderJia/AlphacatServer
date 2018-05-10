package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 记录积分的历史明细
 * @author 161250102
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerCredit {

    private int workerId;
    private int taskId;
    private int valueChange;
    private Timestamp date;

}
