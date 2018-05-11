package com.alphacat.service.impl;

import com.alphacat.mapper.DailyRegisterMapper;
import com.alphacat.mapper.WorkerMapper;
import com.alphacat.pojo.Worker;
import com.alphacat.service.WorkerService;
import com.alphacat.util.BeanMapper;
import com.alphacat.vo.WorkerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkerServiceImpl implements WorkerService {

    @Autowired
    private WorkerMapper workerMapper;
    @Autowired
    private DailyRegisterMapper dailyRegisterMapper;

    @Override
    public List<WorkerVO> getNormalWorkers(int state) {
		List<Worker> ws;
		if(state == -1) {
			ws = workerMapper.getAll();
		} else if(state == 0 || state == 1) {
			ws = workerMapper.getByState(state);
		} else {
			return new List<>();
		}
        return ws.stream().map(BeanMapper::toWorkerVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkerVO> getSortedWorkers() {
        List<Worker> workers = workerMapper.getByState(0);
        workers.sort(Comparator.comparing(Worker::getCredit));
        int length = workers.size() > 10 ? 10 : workers.size();
        List<WorkerVO> results = new LinkedList<>();
        for (int i = 0; i < length; i++) {
            results.add(BeanMapper.toWorkerVO(workers.get(i)));
        }
        return results;
    }

    @Override
    public WorkerVO getWorkerByName(String name) {
        return BeanMapper.toWorkerVO(workerMapper.getByName(name));
    }

	@Override
	public WorkerVO get(int id) {
		return BeanMapper.toWorkerVO(workerMapper.get(id));
	}

    @Override
    public void addWorker(WorkerVO workerVO) {
        Worker worker = BeanMapper.toWorkerPOJO(workerVO);
        int id = workerMapper.getNewId() == null ? 1 : workerMapper.getNewId();
        worker.setId(id);
        workerMapper.add(worker);
    }

    @Override
    public void updateWorker(WorkerVO worker) {
        workerMapper.update(BeanMapper.toWorkerPOJO(worker));
    }

    @Override
    public void setWorkerState(int id, boolean isLocked) {
        if (isLocked) workerMapper.changeState(id, 1);
        else workerMapper.changeState(id, 0);
    }

    @Override
    public boolean hasSameName(String name) {
        return workerMapper.checkName(name);
    }

    @Override
    public void signIn(int id) {
        dailyRegisterMapper.addRecord(id);
    }

    @Override
    public boolean hasSigned(int id) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return dailyRegisterMapper.hasRecord(id, sdf.format(date));
    }

    @Override
    public int getSignDays(int id) {
        int dayCount = 0;
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        while (dailyRegisterMapper.hasRecord(id, sdf.format(date))) {
            dayCount++;
            c.setTime(date);
            int day = c.get(Calendar.DATE);
            c.set(Calendar.DATE, day - 1);
            date = c.getTime();
        }
        return dayCount;
    }
}
