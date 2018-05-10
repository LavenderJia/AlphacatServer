package com.alphacat.vo;

import java.util.List;

/**
 * 一张图片对应的所有标注
 * 工人id可以由session中获取
 * @author 161250102
 */
public class SquaresTagVO {

    private int picIndex;
    private String taskId;
    private List<SquareVO> squares;

}
