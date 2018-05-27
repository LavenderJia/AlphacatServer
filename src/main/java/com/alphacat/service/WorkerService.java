package com.alphacat.service;

import com.alphacat.vo.WorkerVO;

import java.util.List;

public interface WorkerService {

	/**
	 * @param state -1 for all, 0 for active, 1 for locked
	 */
    List<WorkerVO> getByState(int state);

    /**
     * @param num  length of the list returned
     */
    List<WorkerVO> getSortedByCredit(int num);

    /**
     * @param num  length of the list returned
     */
    List<WorkerVO> getSortedByExp(int num);

    WorkerVO getWorkerByName(String name);

	WorkerVO get(int id);

	/**
     * Get credit ranking info of a worker.
     * @return the worker's rank by credit, starting from 1;
     * -1 if worker not found, 0 if worker banned.
     */
	int getCreditRank(int id);

    /**
     * Get exp ranking info of a worker.
     * @return the worker's rank by exp, starting from 1;
     * -1 if worker not found, 0 if worker banned.
     */
	int getExpRank(int id);

	/**
	 * Add a worker account and set up its id.
     * And set its state to 0.
	 */
    void addWorker(WorkerVO worker);

    /**
     * Update a worker's name, birth, sex, email and signature here.
     * Cannot change anything else.
     */
    void updateWorker(WorkerVO worker);

    /**
     * 设置工人的账号状态
     * @param id 工人ID
     * @param isLocked true=被停用
     */
    void setWorkerState(int id, boolean isLocked);
    /**
     * 判断是否有同名的工人
     * @param name 需要检查的名称
     * @return true=有
     */
    boolean hasSameName(String name);

    /**
     * Sign up and add 10 exp.
     * @param id 工人id
     */
    void signUp(int id);

    /**
     * 获取当天是否已经签到
     * @param id
     * @return
     */
    boolean hasSigned(int id);

    /**
     * 获取连续签到天数
     * @param id
     * @return
     */
    int getSignDays(int id);
}
