package com.alphacat.statistics;

import com.alibaba.fastjson.JSONObject;
import com.alphacat.mapper.RequesterMapper;
import com.alphacat.mapper.TaskMapper;
import com.alphacat.mapper.WorkerMapper;
import com.alphacat.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private WorkerMapper workerMapper;
    @Autowired
    private RequesterMapper requesterMapper;
    @Autowired
    private TaskMapper taskMapper;

    @Override
    public JSONObject getWorkerData() {
        JSONObject result = new JSONObject();
        result.put("male", toInt(workerMapper.getNumBySex(0)));
        result.put("female", toInt(workerMapper.getNumBySex(1)));

        int[] ageArray = new int[4];
        ageArray[0] = toInt(workerMapper.getNumByAge(0, 19));
        ageArray[1] = toInt(workerMapper.getNumByAge(19, 30));
        ageArray[2] = toInt(workerMapper.getNumByAge(30, 45));
        ageArray[3] = toInt(workerMapper.getNumByAge(45, 99));
        result.put("ageArray", ageArray);

        int[] workerArray = new int[7];
        int[] activeArray = new int[7];
        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 7; i++) {
            workerArray[6-i] = toInt(workerMapper.getNumUntilDate(sdf.format(date.getTime())));
            activeArray[6-i] = toInt(workerMapper.getActiveNum(sdf.format(date.getTime())));
            date.add(Calendar.DATE, -1);
        }
        result.put("workerArray", workerArray);
        result.put("activeArray", activeArray);

        return result;
    }

    @Override
    public JSONObject getRequesterData() {
        JSONObject result = new JSONObject();
        result.put("num", toInt(requesterMapper.getAllNum()));
        return result;
    }

    @Override
    public JSONObject getTaskData() {
        JSONObject result = new JSONObject();
        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        result.put("taskNum", taskMapper.getNumUntilDate(sdf.format(date)));
        result.put("finishNum", taskMapper.getEndUntilDate(sdf.format(date)));

        int[] taskNumArray = new int[7];
        int[] finishNumArray = new int[7];
        for (int i = 0; i < 7; i++) {
            taskNumArray[6-i] = toInt(taskMapper.getNumUntilDate(sdf.format(date.getTime())));
            finishNumArray[6-i] = toInt(taskMapper.getEndUntilDate(sdf.format(date.getTime())));
            date.add(Calendar.DATE, -1);
        }
        result.put("taskNumArray", taskNumArray);
        result.put("finishNumArray", finishNumArray);

        return result;
    }

    private int toInt(Integer number) {
        return number == null ? 0 : number;
    }
}
