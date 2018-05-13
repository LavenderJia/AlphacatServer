package com.alphacat.user.admin;

import com.alphacat.pojo.Admin;
import com.alphacat.vo.AdminVO;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.stereotype.Component;

@Component
public class AdminConverter {

    private static Mapper mapper = DozerBeanMapperBuilder.create()
            .withMappingFiles("config/dozer-mapping.xml").build();

	public AdminVO toVO(Admin admin) {
		return mapper.map(admin, AdminVO.class);
	}

	public Admin toPOJO(AdminVO adminVO) {
		return mapper.map(adminVO, Admin.class);
	}

}
