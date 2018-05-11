package com.alphacat.service.impl;

import com.alphacat.mapper.RequesterMapper;
import com.alphacat.pojo.Requester;
import com.alphacat.service.RequesterService;
import com.alphacat.util.BeanMapper;
import com.alphacat.vo.RequesterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequesterServiceImpl implements RequesterService{

    @Autowired
    private RequesterMapper requesterMapper;

    @Override
    public List<RequesterVO> getByState(int state) {
		List<Requester> rs;
		if(state == -1) {
			rs = requesterMapper.getAll();
		} else if (state == 0 || state == 1){
			rs = requesterMapper.getByState(state);
		} else {
			return null;
		}
        return rs.stream().map(BeanMapper::toRequesterVO)
                .collect(Collectors.toList());
    }

    @Override
    public RequesterVO getByName(String name) {
        return BeanMapper.toRequesterVO(requesterMapper.getByName(name));
    }

	@Override
	public RequesterVO get(int id) {
		return BeanMapper.toRequesterVO(requesterMapper.get(id));
	}

    @Override
    public void checkRequester(int id, boolean isChecked) {
        if (isChecked) requesterMapper.activate(id);
        else requesterMapper.delete(id);
    }

    @Override
    public void addRequester(RequesterVO requesterVO) {
        Requester requester = BeanMapper.toRequesterPOJO(requesterVO);
        int id = requesterMapper.getNewId() == null ? 1 : requesterMapper.getNewId();
        requester.setId(id);
        requesterMapper.add(requester);
    }

    @Override
    public void updateRequester(RequesterVO requesterVO) {
        requesterMapper.update(BeanMapper.toRequesterPOJO(requesterVO));
    }

    @Override
    public boolean hasSameName(String name) {
        return requesterMapper.checkName(name);
    }
}
