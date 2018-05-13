package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 工人视角的任务，用于显示平台推送的任务
 * @author 161250102
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class W_TaskVO {

    private int id;
    private String name;
    private String endTime;
    private int state; // 0-未参与 1-未完成 2-已完成
    private int workerCount;
    private int creditPerPic;
    private int creditFinished;

}
