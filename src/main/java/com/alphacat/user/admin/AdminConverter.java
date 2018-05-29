package com.alphacat.user.admin;

import com.alphacat.pojo.Admin;
import com.alphacat.vo.AdminVO;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdminConverter {

    @Autowired
    private static Mapper mapper;

	public AdminVO toVO(Admin admin) {
		return mapper.map(admin, AdminVO.class);
	}

	public Admin toPOJO(AdminVO adminVO) {
		return mapper.map(adminVO, Admin.class);
	}

	public List<AdminVO> toVOList(List<Admin> admins) {
	    if(admins == null) {
	        return new ArrayList<>();
        }
        return admins.stream().map(this::toVO).collect(Collectors.toList());
    }

}
