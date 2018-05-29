package com.alphacat.task.schedule;

import com.alphacat.service.CreditService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Do something when a task ends.
 * It should have a non-arg constructor.
 */
public class TaskEndJob implements Job{

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private CreditService service;

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

    /**
     * When a task ends, analyze worker's data, calculate all kinds of
     * accuracy, and transact credits to them.
     */
    private void doTaskEnd(int id) {
        // TODO analyze worker's data and update their accuracy
        System.out.println("Task: " + id);
        service.transact(id);
    }

}
