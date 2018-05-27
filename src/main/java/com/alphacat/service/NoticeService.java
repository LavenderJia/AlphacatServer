package com.alphacat.service;

import com.alphacat.vo.NoticeDetailVO;
import com.alphacat.vo.NoticeVO;
import com.alphacat.vo.UserNoticeBriefVO;

import java.util.List;

public interface NoticeService {

    /**
     * Set up notice's start date and add it into database.
     * @param notice no date or id field
     */
    void add(NoticeVO notice);

    void delete(int id);

    NoticeDetailVO get(int id);

    List<UserNoticeBriefVO> getWorkerNoticeList(int id);

    List<UserNoticeBriefVO> getRequesterNoticeList(int id);

    /**
     * Get notices for admin according to its authority.
     * e.g. Worker admin can only get those that workers can get.
     */
    List<NoticeVO> getAdminNoticeList(int auth);

    void addWorkerNoticeRead(int workerId, int noticeId);

    void addRequesterNoticeRead(int requesterId, int noticeId);

}
