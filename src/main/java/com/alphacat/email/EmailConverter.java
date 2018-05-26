package com.alphacat.email;

import com.alphacat.pojo.AdminEmailBrief;
import com.alphacat.pojo.Email;
import com.alphacat.pojo.UserEmailBrief;
import com.alphacat.vo.AdminEmailBriefVO;
import com.alphacat.vo.EmailVO;
import com.alphacat.vo.UserEmailBriefVO;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmailConverter {

    private static Mapper mapper = DozerBeanMapperBuilder.create()
            .withMappingFiles("config/dozer-mapping.xml").build();

    public List<UserEmailBriefVO> toUserEmailList(List<UserEmailBrief> emails) {
        return emails.stream().map(this::toVO).collect(Collectors.toList());
    }

    public List<AdminEmailBriefVO> toAdminEmailList(List<AdminEmailBrief> emails) {
        return emails.stream().map(this::toVO).collect(Collectors.toList());
    }

    public Email toPOJO(EmailVO email, int id) {
        Email result = mapper.map(email, Email.class);
        result.setId(id);
        return result;
    }

    public EmailVO toVO(Email email) {
        return mapper.map(email, EmailVO.class);
    }

    private UserEmailBriefVO toVO(UserEmailBrief email) {
        return mapper.map(email, UserEmailBriefVO.class);
    }

    private AdminEmailBriefVO toVO(AdminEmailBrief email) {
        return mapper.map(email, AdminEmailBriefVO.class);
    }

}
