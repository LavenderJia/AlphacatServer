package com.alphacat.user.worker;

import com.alphacat.pojo.Worker;
import com.alphacat.vo.WorkerVO;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WorkerConverter {

    @Autowired
    private Mapper mapper;

    public Worker toPOJO(WorkerVO workerVo) {
        return mapper.map(workerVo, Worker.class);
    }

    public WorkerVO toVO(Worker worker) {
        return mapper.map(worker, WorkerVO.class);
    }

    public List<WorkerVO> toVOList(List<Worker> workers) {
        if(workers == null) {
            return new ArrayList<>();
        }
        return workers.stream().map(this::toVO)
                .collect(Collectors.toList());
    }

}
