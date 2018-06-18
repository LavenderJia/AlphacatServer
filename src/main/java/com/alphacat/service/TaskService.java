package com.alphacat.service;

import com.alphacat.vo.*;

import java.util.List;

public interface TaskService {

    List<RequesterTaskVO> getIdle(int requesterId);

    List<RequesterTaskVO> getUnderway(int requesterId);

    List<RequesterTaskVO> getEnded(int requesterId);

    List<RequesterTaskVO> getRequesterTasks(int requesterId);

    /**
     * A test for worker's accuracy. New workers will do them.
     */
    List<AvailableTaskVO> getTestTasks(int workerId);

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
     * Should be equal to {@code add(TaskVO, true)}
     * @return task id
     *
     * @deprecated use {@code add(TaskVO, true)} instead
     */
    int add(TaskVO taskVO);

    /**
     * @param normal whether this task is normal, not draft nor garbage.
     * @return task id
     */
    int add(TaskVO taskVO, boolean normal);

    /**
     * If this task has not started:
     *      almost everything can be changed except for id.
     * If it has started but not ended:
     *      only name, description and endTime can be changed.
     * If it has ended:
     *      nothing can be changed.
     * Should be equal to {@code update(TaskVO, true)}
     *
     * @deprecated use {@code update(TaskVO, true)} instead
     */
    void update(TaskVO taskVO);

    /**
     * @param normal whether this task is normal, not draft nor garbage.
     */
    void update(TaskVO taskVO, boolean normal);

    void delete(int taskId);

    void setToGarbage(int taskId);

    void setToDraft(int taskId);

    List<TaskBriefVO> getDraft(int requesterId);

    List<TaskBriefVO> getGarbage(int requesterId);

}
