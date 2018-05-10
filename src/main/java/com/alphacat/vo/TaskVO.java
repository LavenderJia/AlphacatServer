package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 新增/修改任务时传递的Task对象
 * id不一定显示在界面
 * @author 161250102
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private LabelVO[] labels;

}
