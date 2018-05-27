package com.alphacat.credit.transaction;

import com.alphacat.mapper.CreditMapper;
import com.alphacat.pojo.RequesterCredit;
import com.alphacat.pojo.WorkerCredit;
import com.alphacat.service.CreditService;
import com.alphacat.vo.RequesterCreditVO;
import com.alphacat.vo.WorkerCreditVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditServiceImpl implements CreditService{

    private CreditMapper mapper;
    private CreditConverter converter;

    @Autowired
    public CreditServiceImpl(CreditMapper mapper, CreditConverter converter) {
        this.mapper = mapper;
        this.converter = converter;
    }

    @Override
    public void add(WorkerCreditVO credit, int workerId, int taskId) {
        mapper.add(converter.toPOJO(credit, workerId, taskId));
    }

    @Override
    public List<WorkerCreditVO> getW_CreditTransactions(int workerId) {
        List<WorkerCredit> credits = mapper.getWorkerCredits(workerId);
        return converter.toW_CreditList(credits);
    }

    @Override
    public List<RequesterCreditVO> getR_CreditTransactions(int requesterId) {
        List<RequesterCredit> credits = mapper.getRequesterCredits(requesterId);
        return converter.toR_CreditList(credits);
    }

}
