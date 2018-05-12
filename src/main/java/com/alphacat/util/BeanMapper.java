package com.alphacat.util;

import com.alphacat.pojo.*;
import com.alphacat.vo.*;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 将PO和VO互转的工具类
 * @author 161250102
 */
@Component
public class BeanMapper {

    private static Mapper mapper = DozerBeanMapperBuilder.create().withMappingFiles("config/dozer-mapping.xml").build();

    public static BeanMapper beanMapper;

    @PostConstruct
    public void init() {
        beanMapper = this;
    }

    public static RequesterVO toRequesterVO(Requester requester) {
        return mapper.map(requester, RequesterVO.class);
    }

    public static Requester toRequesterPOJO(RequesterVO requesterVO) {
        return mapper.map(requesterVO, Requester.class);
    }

    public static WorkerVO toWorkerVO(Worker worker) {
        return mapper.map(worker, WorkerVO.class);
    }

    public static Worker toWorkerPOJO(WorkerVO workerVO) {
        return mapper.map(workerVO, Worker.class);
    }

	public static AdminVO toAdminVO(Admin admin) {
		return mapper.map(admin, AdminVO.class);
	}

	public static Admin toAdminPOJO(AdminVO adminVO) {
		return mapper.map(adminVO, Admin.class);
	}

}
