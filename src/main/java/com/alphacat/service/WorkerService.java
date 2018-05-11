package com.alphacat.service;

import com.alphacat.vo.WorkerVO;

import java.util.List;

/**
 * 工人服务接口
 * @author 161250102
 */
public interface WorkerService {

	/**
	 * @param state -1 for all, 0 for active, 1 for locked
	 */
    List<WorkerVO> getByState(int state);

    /**
     * 获取经过排序的工人列表，按积分排序
     * @return 最多返回工人列表前十位
     */
    List<WorkerVO> getSortedWorkers();

    WorkerVO getWorkerByName(String name);

	WorkerVO get(int id);

	/**
	 * Add a worker account and set up its id. 
	 */
    void addWorker(WorkerVO worker);

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
     * 签到一天
     * @param id 工人id
     */
    void signIn(int id);

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
