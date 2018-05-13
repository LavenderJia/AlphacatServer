package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 发起者视角的任务，用于显示任务列表
 * @author 161250102
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class R_TaskVO {

    private int id;
    private String name;
    private String startTime;
    private String endTime;
    private int state; //0 - idle, 1 - doing, 2 - done
    private int workerCount;
    private double tagRate;

}
