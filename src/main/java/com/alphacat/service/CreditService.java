package com.alphacat.service;

import com.alphacat.vo.RequesterCreditVO;
import com.alphacat.vo.WorkerCreditVO;

import java.util.List;

public interface CreditService {

    void add(WorkerCreditVO credit, int workerId, int taskId);

    List<WorkerCreditVO> getW_CreditTransactions(int workerId);

    List<RequesterCreditVO> getR_CreditTransactions(int requesterId);

}
