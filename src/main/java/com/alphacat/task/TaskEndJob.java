package com.alphacat.task;

import com.alphacat.mapper.TaskMapper;
import com.alphacat.pojo.Task;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskEndJob implements Job{

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public void execute(JobExecutionContext context) {
        int id = Integer.parseInt(context.getJobDetail().getKey().getName());
        // test for whether the bean is null
        Task task = taskMapper.get(id);
        System.out.println("Task: " + task.getId());
        // TODO analyze worker's data and update their accuracy
    }

}
