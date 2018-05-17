package com.alphacat.tag;

import com.alphacat.mapper.TaskMapper;
import com.alphacat.mapper.WorkerCreditMapper;
import com.alphacat.mapper.WorkerMapper;
import com.alphacat.pojo.Task;
import com.alphacat.pojo.WorkerCredit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Calendar;

@Component
public class CreditTransactor {

    @Autowired
    private WorkerMapper workerMapper;
    @Autowired
    private WorkerCreditMapper creditMapper;
    @Autowired
    private TaskMapper taskMapper;

    /**
     * Credit transaction when a worker has tagged a picture.
     * @param minus whether to decrease worker's credit,
     *              true to decrease, false to increase
     */
    public void picTransact(int taskId, int workerId, boolean minus) {
        Task task = taskMapper.get(taskId);
        int value = task.getCreditPerPic();
        if(minus) {
            value = -value;
        }
        workerMapper.addCredit(workerId, value);
        Timestamp now = new Timestamp(Calendar.getInstance().getTimeInMillis());
        WorkerCredit credit = new WorkerCredit(workerId, taskId, value, now);
        creditMapper.add(credit);
    }

    /**
     * Credit transaction when a worker has completed a task.
     * @param minus whether to decrease worker's credit,
     *              true to decrease, false to increase
     */
    public void taskTransact(int taskId, int workerId, boolean minus) {
        Task task = taskMapper.get(taskId);
        workerMapper.addCredit(workerId, task.getCreditFinished());
        Timestamp now = new Timestamp(Calendar.getInstance().getTimeInMillis());
        WorkerCredit credit = new WorkerCredit(workerId, taskId, task.getCreditFinished(), now);
        creditMapper.add(credit);
    }

}
