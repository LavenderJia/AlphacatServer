package com.alphacat.user.worker;

import com.alphacat.mapper.DailyRegisterMapper;
import com.alphacat.mapper.WorkerMapper;
import com.alphacat.pojo.Worker;
import com.alphacat.service.WorkerService;
import com.alphacat.vo.WorkerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WorkerServiceImpl implements WorkerService {

    @Autowired
    private WorkerMapper workerMapper;
    @Autowired
    private WorkerConverter workerConverter;
    @Autowired
    private DailyRegisterMapper dailyRegisterMapper;

    @Override
    public List<WorkerVO> getByState(int state) {
		List<Worker> ws;
		if(state == -1) {
			ws = workerMapper.getAll();
		} else if(state == 0 || state == 1) {
			ws = workerMapper.getByState(state);
		} else {
			return new ArrayList<>();
		}
        return workerConverter.toVOList(ws);
    }

    @Override
    public List<WorkerVO> getSortedByCredit(int number) {
        List<Worker> workers = workerMapper.getSortedByCredit(number);
        return workerConverter.toVOList(workers);
    }

    @Override
    public List<WorkerVO> getSortedByExp(int number) {
        List<Worker> workers = workerMapper.getSortedByExp(number);
        return workerConverter.toVOList(workers);
    }

    @Override
    public WorkerVO getWorkerByName(String name) {
        return workerConverter.toVO(workerMapper.getByName(name));
    }

	@Override
	public WorkerVO get(int id) {
		return workerConverter.toVO(workerMapper.get(id));
	}

	@Override
    public int getCreditRank(int id) {
        Worker worker = workerMapper.get(id);
        if(worker == null) return -1;
        if(worker.getState() == 1) return 0;
        return workerMapper.getCreditRank(id);
    }

    @Override
    public int getExpRank(int id) {
        Worker worker = workerMapper.get(id);
        if(worker == null) return -1;
        if(worker.getState() == 1) return 0;
        return workerMapper.getExpRank(id);
    }

    @Override
    public void addWorker(WorkerVO workerVO) {
        Worker worker = workerConverter.toPOJO(workerVO);
        int id = workerMapper.getNewId() == null ? 1 : workerMapper.getNewId();
        worker.setId(id);
        worker.setState(2);
        workerMapper.add(worker);
    }

    @Override
    public void updateWorker(WorkerVO worker) {
        workerMapper.update(workerConverter.toPOJO(worker));
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
    public void signUp(int id) {
        if(hasSigned(id)) {
            return;
        }
        dailyRegisterMapper.addRecord(id);
        workerMapper.addExp(id, 10);
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
