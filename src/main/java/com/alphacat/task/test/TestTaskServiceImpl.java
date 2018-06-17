package com.alphacat.task.test;

import com.alphacat.mapper.TaskRecordMapper;
import com.alphacat.mapper.WorkerMapper;
import com.alphacat.pojo.TaskRecord;
import com.alphacat.service.CreditService;
import com.alphacat.service.SquareService;
import com.alphacat.service.TestTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestTaskServiceImpl implements TestTaskService{

    private TaskRecordMapper recordMapper;
    private CreditService creditService;
    private WorkerMapper workerMapper;
    private SquareService squareService;

    @Autowired
    public TestTaskServiceImpl(TaskRecordMapper recordMapper, WorkerMapper workerMapper,
                               CreditService creditService, SquareService squareService) {
        this.recordMapper = recordMapper;
        this.creditService = creditService;
        this.workerMapper = workerMapper;
        this.squareService = squareService;
    }

    @Override
    public boolean test(int taskId, int workerId) {
        boolean correct = squareService.testAnswer(taskId, workerId);
        if(correct) {
            creditService.transact(taskId, workerId);
        }
        return correct;
    }

    @Override
    public boolean testAllFinished(int workerId) {
        boolean completed = recordMapper.getRectAccuracy(1, workerId) != null
                && recordMapper.getRectAccuracy(2, workerId) != null;
        if(completed) {
            workerMapper.changeState(workerId, 0);
        }
        return completed;
    }

}
