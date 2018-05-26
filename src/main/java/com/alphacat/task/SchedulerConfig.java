package com.alphacat.task;

import com.alphacat.mapper.TaskMapper;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfig {

    @Bean
    public Scheduler getScheduler() {
        try {
            return StdSchedulerFactory.getDefaultScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Bean
    public TaskEndScheduler getTaskEndScheduler(TaskMapper taskMapper) {
        return new TaskEndScheduler(taskMapper, getScheduler());
    }

}
