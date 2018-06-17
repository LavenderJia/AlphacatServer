package com.alphacat.credit.transaction;

import com.alphacat.mapper.CreditMapper;
import com.alphacat.mapper.TaskMapper;
import com.alphacat.mapper.TaskRecordMapper;
import com.alphacat.mapper.WorkerMapper;
import com.alphacat.pojo.*;
import com.alphacat.service.CreditService;
import com.alphacat.vo.RequesterCreditVO;
import com.alphacat.vo.WorkerCreditVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Service
public class CreditServiceImpl implements CreditService{

    private CreditMapper creditMapper;
    private CreditConverter converter;
    private WorkerMapper workerMapper;
    private TaskMapper taskMapper;
    private TaskRecordMapper recordMapper;

    @Autowired
    public CreditServiceImpl(CreditMapper creditMapper, CreditConverter converter,
                             WorkerMapper workerMapper, TaskMapper taskMapper,
                             TaskRecordMapper recordMapper) {
        this.creditMapper = creditMapper;
        this.converter = converter;
        this.workerMapper = workerMapper;
        this.taskMapper = taskMapper;
        this.recordMapper = recordMapper;
    }

    @Override
    public void transact(int taskId) {
        recordMapper.getByTask(taskId).forEach(this::transact);
    }

    @Override
    public void transact(int taskId, int workerId) {
        this.transact(recordMapper.get(workerId, taskId));
    }

    /**
     * Single transact according to the record of a worker in a task.
     */
    private void transact(TaskRecord record) {
        int taskId = record.getTaskId();
        Task task = taskMapper.get(taskId);
        int creditPerPic = task.getCreditPerPic();
        int creditFinished = task.getCreditFinished();
        int change = record.getPicDoneNum() * creditPerPic;
        if(record.getPicDoneNum() == record.getPicOrder().length() / 2) {
            change += creditFinished;
        }
        int workerId = record.getWorkerId();
        Double rectAccuracy = recordMapper.getRectAccuracy(taskId, workerId);
        Double labelAccuracy = recordMapper.getLabelAccuracy(taskId, workerId);
        if(rectAccuracy == null || labelAccuracy == null) {
            throw new NullPointerException("Answer hasn't been estimated for worker: " + workerId + " in task: " + taskId);
        }
        // final credit earned = credit to be earned * average of accuracy
        change *= (rectAccuracy + labelAccuracy) / 2;
        Worker worker = workerMapper.get(workerId);
        int credit = worker.getCredit() + change;
        Date now = new Date(Calendar.getInstance().getTimeInMillis());
        WorkerCredit c = new WorkerCredit(workerId, taskId,
                // The #taskName below does NOT make any sense.
                // Credit mapper doesn't count it when adding a new record.
                "", change, now, credit);
        creditMapper.add(c);
        workerMapper.addCredit(workerId, change);
    }

    @Override
    public List<WorkerCreditVO> getW_CreditTransactions(int workerId) {
        List<WorkerCredit> credits = creditMapper.getWorkerCredits(workerId);
        return converter.toW_CreditList(credits);
    }

    @Override
    public List<RequesterCreditVO> getR_CreditTransactions(int requesterId) {
        List<RequesterCredit> credits = creditMapper.getRequesterCredits(requesterId);
        return converter.toR_CreditList(credits);
    }

}
