package com.alphacat.credit.transaction;

import com.alphacat.pojo.RequesterCredit;
import com.alphacat.pojo.WorkerCredit;
import com.alphacat.vo.RequesterCreditVO;
import com.alphacat.vo.WorkerCreditVO;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CreditConverter {

    private Mapper mapper = DozerBeanMapperBuilder.create()
            .withMappingFiles("config/dozer-mapping.xml").build();

    public WorkerCredit toPOJO(WorkerCreditVO credit, int workerId, int taskId) {
        WorkerCredit result = mapper.map(credit, WorkerCredit.class);
        result.setTaskId(taskId);
        result.setWorkerId(workerId);
        return result;
    }

    public List<WorkerCreditVO> toW_CreditList(List<WorkerCredit> credits) {
        return credits.stream().map(this::toVO).collect(Collectors.toList());
    }

    public List<RequesterCreditVO> toR_CreditList(List<RequesterCredit> credits) {
        return credits.stream().map(this::toVO).collect(Collectors.toList());
    }

    private WorkerCreditVO toVO(WorkerCredit credit) {
        WorkerCreditVO result = mapper.map(credit, WorkerCreditVO.class);
        result.setInfo(credit.getTaskName() + "任务结算");
        return result;
    }

    private RequesterCreditVO toVO(RequesterCredit credit) {
        return mapper.map(credit, RequesterCreditVO.class);
    }

}
