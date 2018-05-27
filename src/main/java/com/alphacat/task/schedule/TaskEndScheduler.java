package com.alphacat.task.schedule;

import com.alphacat.mapper.TaskMapper;
import com.alphacat.pojo.Task;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

/**
 * This scheduler adds task-end jobs into schedule.
 * @see TaskEndJob
 */
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
        tasks.forEach(this::scheduleSingleJob);
    }

    /**
     * This method adds a single job into a schedule.
     * The job #TaskEndJob itself will self-delete when job is done.
     */
    private void scheduleSingleJob(Task task) {
        String id = task.getId() + "";
        String group = "task-end" + id;
        JobDetail jobDetail = JobBuilder.newJob(TaskEndJob.class)
                .withIdentity(id, group)
                .build();
        // trigger startDate should be the day after task-end day
        Date endTime = task.getEndTime();
        Calendar c = Calendar.getInstance();
        c.setTime(endTime);
        c.add(Calendar.DAY_OF_MONTH, 1);
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(id, group)
                // it will only trigger a job once
                .startAt(c.getTime())
                .build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        } catch(SchedulerException e) {
            e.printStackTrace();
        }
    }

}
