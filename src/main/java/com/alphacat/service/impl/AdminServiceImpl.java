package com.alphacat.service.impl;

import com.alphacat.service.AdminService;
import com.alphacat.mapper.AdminMapper;
import com.alphacat.pojo.Admin;
import com.alphacat.vo.AdminVO;
import com.alphacat.util.BeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService{

	@Autowired
	private AdminMapper adminMapper;

	@Override
	public List<AdminVO> getAll() {
		List<Admin> admins = adminMapper.getAll();
		return admins.stream().map(BeanMapper::toAdminVO)
				.collect(Collectors.toList());
	}

	@Override
	public List<AdminVO> getByAuth(int auth) {
		List<Admin> admins = adminMapper.getByAuth(auth);
		return admins.stream().map(BeanMapper::toAdminVO)
				.collect(Collectors.toList());
	}

	@Override
	public AdminVO get(int id) {
		Admin admin = adminMapper.get(id);
		return BeanMapper.toAdminVO(admin);
	}

	@Override
	public void add(AdminVO adminVO) {
		Integer id = adminMapper.getNewId();
		if(id == null) {
			id = 1;
		}
		adminVO.setId(id);
		Admin admin = BeanMapper.toAdminPOJO(adminVO);
		adminMapper.add(admin);
	}

	@Override
	public void update(AdminVO adminVO) {
		Admin admin = BeanMapper.toAdminPOJO(adminVO);
		adminMapper.update(admin);
	}

	@Override
	public void delete(int id) {
		adminMapper.delete(id);
	}

	@Override
	public void setAuth(int id, int auth) {
	    adminMapper.setAuth(id, auth);
	}

}
