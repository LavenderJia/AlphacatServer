package com.alphacat.email;

import com.alphacat.mapper.EmailMapper;
import com.alphacat.pojo.AdminEmailBrief;
import com.alphacat.pojo.Email;
import com.alphacat.pojo.UserEmailBrief;
import com.alphacat.service.EmailService;
import com.alphacat.vo.AdminEmailBriefVO;
import com.alphacat.vo.EmailVO;
import com.alphacat.vo.UserEmailBriefVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService{

    private EmailMapper mapper;
    private EmailConverter converter;

    @Autowired
    public EmailServiceImpl(EmailMapper mapper, EmailConverter converter) {
        this.mapper = mapper;
        this.converter = converter;
    }

    @Override
    public void add(EmailVO email) {
        Integer id = mapper.getNewId();
        if(id == null) {
            throw new NullPointerException("Cannot generate new email id.");
        }
        email.setDate(new Timestamp(Calendar.getInstance().getTimeInMillis()).toString());
        Email e = converter.toPOJO(email, id);
        mapper.add(e);
    }

    @Override
    public void delete(int id) {
        mapper.delete(id);
    }

    @Override
    public EmailVO get(int id) {
        Email email = mapper.get(id);
        if(email == null) {
            throw new NullPointerException("No such email with id: " + id);
        }
        return converter.toVO(email);
    }

    @Override
    public List<UserEmailBriefVO> getWorkerEmailList(int id) {
        List<UserEmailBrief> workerEmails = mapper.getWorkerEmails(id);
        return converter.toUserEmailList(workerEmails);
    }

    @Override
    public List<UserEmailBriefVO> getRequesterEmailList(int id) {
        List<UserEmailBrief> requesterEmails = mapper.getRequesterEmails(id);
        return converter.toUserEmailList(requesterEmails);
    }

    @Override
    public List<AdminEmailBriefVO> getAdminEmailList(int auth) {
        List<AdminEmailBrief> adminEmails;
        switch (auth) {
            case 0: adminEmails = mapper.getAll(); break;
            case 1: adminEmails = mapper.getAdminEmails(1); break;
            case 2: adminEmails = mapper.getAdminEmails(2); break;
            default: throw new AuthorityException(auth);
        }
        return converter.toAdminEmailList(adminEmails);
    }

    public class AuthorityException extends RuntimeException {
        public AuthorityException(int auth) {
            super("Authority number is incorrect: " + auth);
        }
    }

}
