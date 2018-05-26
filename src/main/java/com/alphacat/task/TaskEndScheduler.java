package com.alphacat.task;

import com.alphacat.mapper.TaskMapper;
import com.alphacat.pojo.Task;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.util.resources.cldr.aa.CalendarData_aa_DJ;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Component
public class TaskEndScheduler {

    private TaskMapper taskMapper;
    private Scheduler scheduler;

    @Autowired
    public TaskEndScheduler(TaskMapper taskMapper, Scheduler scheduler) {
        this.taskMapper = taskMapper;
        this.scheduler = scheduler;
        scheduleJobs();
    }

    private void scheduleJobs() {
        List<Task> tasks = taskMapper.getNotEnded();
        tasks.forEach(t -> scheduleSingleJob(t));
    }

    private void scheduleSingleJob(Task task) {
        String id = task.getId() + "";
        JobKey jobKey = JobKey.jobKey(id, "taskEndJob" + id);
        try {
            scheduler.deleteJob(jobKey);
        } catch(SchedulerException e) {
            e.printStackTrace();
        }
        JobDetail jobDetail = JobBuilder.newJob(TaskEndJob.class)
                .withIdentity(jobKey)
                .build();
        // trigger time should the day after task end day
/*
        Date endTime = task.getEndTime();
        Calendar c = Calendar.getInstance();
        c.setTime(endTime);
        c.add(Calendar.DAY_OF_MONTH, 1);
*/
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, 10);
        String cronExpr = buildCronExpr(c);
        System.out.println(cronExpr);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpr);
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("t" + task.getId(), "task-end-trigger" + task.getId())
                .withSchedule(scheduleBuilder)
                .build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch(SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Impulsive cron expression. It stands for just a single day.
     */
    private String buildCronExpr(Calendar c) {
        String cronExpr = "";
        cronExpr += c.get(Calendar.SECOND) + " ";
        cronExpr += c.get(Calendar.MINUTE) + " ";
        cronExpr += c.get(Calendar.HOUR_OF_DAY) + " ";
        cronExpr += c.get(Calendar.DAY_OF_MONTH) + " ";
        cronExpr += c.get(Calendar.MONTH) + 1 + " ";
        cronExpr += "? ";
        cronExpr += c.get(Calendar.YEAR);
        return cronExpr;
    }

}
