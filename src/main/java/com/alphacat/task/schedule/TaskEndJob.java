package com.alphacat.task.schedule;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Do something when a task ends.
 * It should have a non-arg constructor.
 */
@Component
public class TaskEndJob implements Job{

    @Autowired
    private Scheduler scheduler;

    @Override
    public void execute(JobExecutionContext context) {
        JobKey key = context.getJobDetail().getKey();
        doTaskEnd(Integer.parseInt(key.getName()));
        try {
            scheduler.deleteJob(key);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private void doTaskEnd(int id) {
        // TODO analyze worker's data and update their accuracy
        // TODO also transact credits
        System.out.println("Task: " + id);
    }

}
