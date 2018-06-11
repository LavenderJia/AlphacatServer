package com.alphacat.user.requester;

import com.alphacat.mapper.RequesterMapper;
import com.alphacat.pojo.Requester;
import com.alphacat.service.RequesterService;
import com.alphacat.vo.RequesterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequesterServiceImpl implements RequesterService{

    @Autowired
    private RequesterMapper requesterMapper;
    @Autowired
    private RequesterConverter requesterConverter;

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
        return requesterConverter.toVOList(rs);
    }

    @Override
    public RequesterVO getByName(String name) {
        return requesterConverter.toVO(requesterMapper.getByName(name));
    }

	@Override
	public RequesterVO get(int id) {
		return requesterConverter.toVO(requesterMapper.get(id));
	}

    @Override
    public void checkRequester(int id, boolean isChecked) {
        if (isChecked) requesterMapper.activate(id);
        else requesterMapper.delete(id);
    }

    @Override
    public void addRequester(RequesterVO requesterVO) {
        Requester requester = requesterConverter.toPOJO(requesterVO);
        int id = requesterMapper.getNewId() == null ? 1 : requesterMapper.getNewId();
        requester.setId(id);
        requester.setState(1);
        requesterMapper.add(requester);
    }

    @Override
    public void updateRequester(RequesterVO requesterVO) {
        requesterMapper.update(requesterConverter.toPOJO(requesterVO));
    }

    @Override
    public boolean hasSameName(String name) {
        return requesterMapper.checkName(name);
    }
}
