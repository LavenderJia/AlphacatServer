package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Basic info and settings about task. It DOES include labels.
 * Used in adding or updating a task.
 * @author 161250192
 * @see LabelVO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TaskVO {

    private int id;
    private int requesterId;
    private String name;
    private String description;
    private int creditPerPic;
    private int creditFinished;
    private int method; //标注方式：1=单框，2=多框，3=不规则
    private boolean hasWholeLabel;
    private String startTime;
    private String endTime;
    private List<LabelVO> labels;

}
