package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 方框标注：map中存放标签-结果键值对
 * @author 161250102
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SquareVO {

    private int squareIndex;
    private int x;
    private int y;
    private int w;
    private int h;
    private Map<String, String> labelData;
    private String description; //整体描述

}
