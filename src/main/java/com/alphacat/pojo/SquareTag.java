package com.alphacat.pojo;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 方框标注
 * @author 161250102
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
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
