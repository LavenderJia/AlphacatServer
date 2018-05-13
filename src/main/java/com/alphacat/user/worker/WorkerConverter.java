package com.alphacat.user.worker;

import com.alphacat.pojo.Worker;
import com.alphacat.vo.WorkerVO;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.stereotype.Component;

@Component
public class WorkerConverter {

    private Mapper mapper = DozerBeanMapperBuilder.create()
            .withMappingFiles("config/dozer-mapping.xml").build();

    public Worker toPOJO(WorkerVO workerVo) {
        return mapper.map(workerVo, Worker.class);
    }

    public WorkerVO toVO(Worker worker) {
        return mapper.map(worker, WorkerVO.class);
    }

}
