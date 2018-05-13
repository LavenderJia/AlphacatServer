package com.alphacat.service;

import com.alphacat.vo.HistoryTaskVO;
import com.alphacat.vo.R_TaskVO;
import com.alphacat.vo.TaskVO;
import com.alphacat.vo.W_TaskVO;

import java.util.List;

public interface TaskService {

    /**
     * 获取发起者自己发布的任务列表
     * @param requesterId 发起者的ID
     * @return 任务列表
     */
    List<R_TaskVO> getR(int requesterId);

    /**
     * 发起者修改已有任务时获取任务对象
     * @param taskId 任务ID
     * @return 返回的任务对象
     */
    TaskVO get(int taskId);

    /**
     * 获取工人可以参与的任务列表
     * @param workerId 工人的Id
     * @return 任务列表
     */
    List<W_TaskVO> getW(int workerId);

    /**
     * 获取工人参与过的历史任务列表
     * @param workerId 工人ID
     * @return 任务列表
     */
    List<HistoryTaskVO> getHistoryTasks(int workerId);

    /**
     * Allocate a new task id and add this task, along with its labels.
     * @return task id
     * @throws  NullPointerException new id allocation failed
     */
    int add(TaskVO taskVO);

    void update(TaskVO taskVO);

    void delete(int taskId);
}
