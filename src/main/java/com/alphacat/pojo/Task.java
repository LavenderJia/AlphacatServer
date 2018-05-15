package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * The basic info and settings about a task. It does NOT include the labels of this task.
 * No dynamic info here. Always generate dynamic info by yourself when needed.
 * @author 161250192
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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
