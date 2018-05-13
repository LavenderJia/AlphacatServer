package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 工人-历史任务显示，只显示自己参与过的任务
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class HistoryTaskVO {

    private int id;
    private String name;
    private String startTime;
    private String endTime;
    private int earnedCredit; //赚取的积分

}
