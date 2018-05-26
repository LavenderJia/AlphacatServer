package com.alphacat.service;

import com.alphacat.vo.AdminEmailBriefVO;
import com.alphacat.vo.EmailVO;
import com.alphacat.vo.UserEmailBriefVO;

import java.util.List;

public interface EmailService {

    /**
     * Set up email's date and add it into database.
     * @param email have no date field
     */
    void add(EmailVO email);

    void delete(int id);

    EmailVO get(int id);

    List<UserEmailBriefVO> getWorkerEmailList(int id);

    List<UserEmailBriefVO> getRequesterEmailList(int id);

    /**
     * Get emails for admin according to its authority.
     * Worker admin can only get those that workers can get.
     */
    List<AdminEmailBriefVO> getAdminEmailList(int auth);

}
