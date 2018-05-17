package com.alphacat.service;

import com.alphacat.vo.SquareVO;

import java.util.List;

public interface SquareService {

    /**
     * 获取工人在图片上标注的所有方框
     * @param taskId 任务id
     * @param workerId 工人id
     * @param picIndex 图片序号
     * @return 方框列表
     */
    List<SquareVO> getSquares(int workerId, int taskId, int picIndex);

    /**
     * 保存工人在图片上的标注
     * @param squares 标注列表
     */
    void saveSqaures(List<SquareVO> squares, int workerId, int taskId, int picIndex);

}
