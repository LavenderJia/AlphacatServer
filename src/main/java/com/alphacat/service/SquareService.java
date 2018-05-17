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
     * Save workers square tags and if it is the first time that the worker
     * has tagged this picture, the worker gains credits and update worker's
     * record.
     * @param squares 标注列表
     */
    void saveSquares(List<SquareVO> squares, int workerId, int taskId, int picIndex);

    /**
     * Clear square data of the certain worker in the certain task on the certain picture.
     */
    void deleteSquares(int workerId, int taskId, int picIndex);

}
