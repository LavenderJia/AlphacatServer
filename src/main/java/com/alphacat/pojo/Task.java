package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * 任务
 * @author 161250102
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    private int id;
    private int requesterId;
    private String name;
    private String description;
    private int creditPerPic;
    private int creditFinished;
    private int method; //标注方式：1=单框，2=多框，3=不规则
    private boolean hasWholeLabel;
    private Date startTime;
    private Date endTime;

}
