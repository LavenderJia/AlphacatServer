package com.alphacat.user.requester;

import com.alphacat.pojo.Requester;
import com.alphacat.vo.RequesterVO;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequesterConverter {

    @Autowired
    private Mapper mapper;

    public Requester toPOJO(RequesterVO requesterVO) {
        return mapper.map(requesterVO, Requester.class);
    }

    public RequesterVO toVO(Requester requester) {
        return mapper.map(requester, RequesterVO.class);
    }

    public List<RequesterVO> toVOList(List<Requester> requesters) {
        if(requesters == null) {
            return new ArrayList<>();
        }
        return requesters.stream().map(this::toVO).collect(Collectors.toList());
    }

}
