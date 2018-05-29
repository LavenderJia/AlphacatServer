package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 方框标注
 * @author 161250102
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SquareTag {

    private int workerId;
    private int taskId;
    private int picIndex;
    private int squareIndex; //框序号,0开始
    private int x;
    private int y;
    private int h;
    private int w;
    private String labelData;
    private String description; //整体描述,如果有的话

}
