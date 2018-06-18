package com.alphacat.service;

import com.alibaba.fastjson.JSONObject;

public interface StatisticsService {

    JSONObject getWorkerData();

    JSONObject getRequesterData();

    JSONObject getTaskData();
}
