package com.alphacat.statistics;

import com.alphacat.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/statistics")
@RestController
public class StatisticsController {

    @Autowired
    private StatisticsService service;

    @GetMapping("/worker")
    public String workerData() {
        return service.getWorkerData().toString();
    }

    @GetMapping("/requester")
    public String requesterData() {
        return service.getRequesterData().toString();
    }

    @GetMapping("/task")
    public String taskData() {
        return service.getTaskData().toString();
    }
}
