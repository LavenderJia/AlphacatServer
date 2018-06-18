package com.alphacat.service;

import com.alphacat.vo.RequesterCreditVO;
import com.alphacat.vo.WorkerCreditVO;

import java.util.List;

public interface CreditService {

    /**
     * Transact credits to workers when a task ends.
     */
    void transact(int taskId);

    /**
     * Transact credits to a worker of a task.
     */
    void transact(int taskId, int workerId);

    /**
     * Get worker's credit transaction records.
     * In date-descending order.
     */
    List<WorkerCreditVO> getW_CreditTransactions(int workerId);

    /**
     * Get requester's credit transaction records.
     * In date-descending order.
     */
    List<RequesterCreditVO> getR_CreditTransactions(int requesterId);

}
