package com.alphacat.service;

import com.alphacat.pojo.AvailableTask;
import com.alphacat.vo.*;

import java.util.List;

public interface TaskService {

    List<IdleTaskVO> getIdle(int requesterId);

    List<UnderwayTaskVO> getUnderway(int requesterId);

    List<EndedTaskVO> getEnded(int requesterId);

    /**
     * Get tasks that's available but the worker DOES NOT take part in.
     */
    List<AvailableTaskVO> getAvailable(int workerId);

    /**
     * Get tasks that the worker is taking part in.
     */
    List<AvailableTaskVO> getPartaking(int workerId);

    List<HistoryTaskVO> getHistory(int workerId);

    /**
     * 发起者修改已有任务时获取任务对象
     * @param taskId 任务ID
     * @return 返回的任务对象
     */
    TaskVO get(int taskId);

    /**
     * Allocate a new task id and add this task, along with its labels.
     * @return task id
     * @throws  NullPointerException new id allocation failed
     */
    int add(TaskVO taskVO);

    /**
     * If this task has not started:
     *      almost everything can be changed except for id.
     * If it has started but not ended:
     *      only name, description and endTime can be changed.
     * If it has ended:
     *      nothing can be changed.
     */
    void update(TaskVO taskVO);

    void delete(int taskId);
}
