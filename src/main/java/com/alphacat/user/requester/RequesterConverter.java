package com.alphacat.user.requester;

import com.alphacat.pojo.Requester;
import com.alphacat.vo.RequesterVO;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.stereotype.Component;

@Component
public class RequesterConverter {

    private Mapper mapper = DozerBeanMapperBuilder.create()
            .withMappingFiles("config/dozer-mapping.xml").build();

    public Requester toPOJO(RequesterVO requesterVO) {
        return mapper.map(requesterVO, Requester.class);
    }

    public RequesterVO toVO(Requester requester) {
        return mapper.map(requester, RequesterVO.class);
    }

}
