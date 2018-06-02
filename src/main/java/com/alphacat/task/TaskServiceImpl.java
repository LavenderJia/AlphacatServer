package com.alphacat.task;

import com.alphacat.mapper.LabelMapper;
import com.alphacat.mapper.TaskMapper;
import com.alphacat.pojo.*;
import com.alphacat.service.TaskService;
import com.alphacat.task.schedule.TaskEndScheduler;
import com.alphacat.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskConverter taskConverter;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private LabelMapper labelMapper;
    @Autowired
    private TaskEndScheduler scheduler;

    @Override
    public List<RequesterTaskVO> getIdle(int requesterId) {
        List<RequesterTask> tasks = taskMapper.getIdleTasks(requesterId);
        return taskConverter.toRequesterVOList(tasks);
    }

    @Override
    public List<RequesterTaskVO> getUnderway(int requesterId) {
        List<RequesterTask> tasks = taskMapper.getUnderwayTasks(requesterId);
        return taskConverter.toRequesterVOList(tasks);
    }

    @Override
    public List<RequesterTaskVO> getEnded(int requesterId) {
        List<RequesterTask> tasks = taskMapper.getEndedTask(requesterId);
        return taskConverter.toRequesterVOList(tasks);
    }

    @Override
    public List<RequesterTaskVO> getRequesterTasks(int requesterId) {
        List<RequesterTask> tasks = taskMapper.getByRequester(requesterId);
        return taskConverter.toRequesterVOList(tasks);
    }

    @Override
    public TaskVO get(int id) {
        return taskConverter.toVO(taskMapper.get(id));
    }

    @Override
    public List<AvailableTaskVO> getTestTasks() {
        List<AvailableTask> tasks = taskMapper.getTestTask();
        return taskConverter.toAvailableVOList(tasks);
    }

    @Override
    public List<AvailableTaskVO> getAvailable(int workerId) {
        List<AvailableTask> tasks = taskMapper.getAvailableTask(workerId);
        return taskConverter.toAvailableVOList(tasks);
    }

    @Override
    public List<AvailableTaskVO> getPartaking(int workerId) {
        List<AvailableTask> tasks = taskMapper.getPartakingTask(workerId);
        return taskConverter.toAvailableVOList(tasks);
    }

    @Override
    public List<HistoryTaskVO> getHistory(int workerId) {
        List<HistoryTask> tasks = taskMapper.getHistoryTasks(workerId);
        return taskConverter.toHistoryVOList(tasks);
    }

    @Override
    public int add(TaskVO taskVO) {
        // set up new task id
        Integer tid = taskMapper.getNewId();
        int id = tid == null ? 1 : tid;
        taskVO.setId(id);
        Task task = taskConverter.toPOJO(taskVO);
        // check start time and end time
        checkTaskDate(task);
        // add task's basic info and settings
        taskMapper.add(task);
        // add labels
        taskVO.getLabels().forEach(l -> labelMapper.add(taskConverter.toPOJO(l, id)));
        // add a new task end job
        scheduler.scheduleSingleJob(task);
        return id;
    }

    @Override
    public void update(TaskVO taskVO) {
        int id = taskVO.getId();
        Task origin = taskMapper.get(id);
        Date now = new Date(Calendar.getInstance().getTimeInMillis());
        // 1. change everything if it's not started
        if(now.before(origin.getStartTime())) {
            Task task = taskConverter.toPOJO(taskVO);
            // check date first
            checkTaskDate(task);
            taskMapper.update(task);
            labelMapper.delete(taskVO.getId());
            taskVO.getLabels().
                    forEach(l -> labelMapper.add(taskConverter.toPOJO(l, id)));
            // then update the task end job
            scheduler.scheduleSingleJob(task);
            return;
        }
        // 2. change only name, description and endTime if underway
        if(now.after(origin.getStartTime()) && now.before(origin.getEndTime())) {
            // check date first
            Date endTime = Date.valueOf(taskVO.getEndTime());
            if(now.after(endTime)) {
                throw new IllegalArgumentException("End time cannot too early: " + endTime);
            }
            origin.setName(taskVO.getName());
            origin.setDescription(taskVO.getDescription());
            origin.setEndTime(endTime);
            taskMapper.update(origin);
            // update the task end job
            scheduler.scheduleSingleJob(origin);
        }
        // 3. change nothing if ended
    }

    /**
     * Just multiDelete the task. Mysql will multiDelete other related data for you.
     */
    @Override
    public void delete(int id) {
        taskMapper.delete(id);
    }

    /**
     * If start time or end time not valid, throw an illegal argument exception.
     */
    private void checkTaskDate(Task task) {
        Date now = new Date(Calendar.getInstance().getTimeInMillis());
        Date startTime = task.getStartTime();
        Date endTime = task.getEndTime();
        if(!now.before(startTime) || startTime.after(endTime)) {
            int id = task.getId();
            throw new IllegalArgumentException("Start time or end time is ILLEGAL for task: " + id);
        }
    }

}
