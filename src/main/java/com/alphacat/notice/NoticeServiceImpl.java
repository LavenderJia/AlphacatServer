package com.alphacat.notice;

import com.alphacat.mapper.NoticeMapper;
import com.alphacat.pojo.Notice;
import com.alphacat.pojo.UserNoticeBrief;
import com.alphacat.service.NoticeService;
import com.alphacat.vo.NoticeDetailVO;
import com.alphacat.vo.NoticeVO;
import com.alphacat.vo.UserNoticeBriefVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    private NoticeMapper mapper;
    private NoticeConverter converter;

    @Autowired
    public NoticeServiceImpl(NoticeMapper mapper, NoticeConverter converter) {
        this.mapper = mapper;
        this.converter = converter;
    }

    @Override
    public void add(NoticeVO email) {
        Integer id = mapper.getNewId();
        if(id == null) {
            throw new NullPointerException("Cannot generate new notice id.");
        }
        email.setStartDate(new Timestamp(Calendar.getInstance().getTimeInMillis()).toString());
        Notice e = converter.toPOJO(email);
        mapper.add(e);
    }

    @Override
    public void delete(int id) {
        mapper.delete(id);
    }

    @Override
    public NoticeDetailVO get(int id) {
        Notice notice = mapper.get(id);
        if(notice == null) {
            throw new NullPointerException("No such notice with id: " + id);
        }
        return converter.toDetailVO(notice);
    }

    @Override
    public List<UserNoticeBriefVO> getWorkerNoticeList(int id) {
        List<UserNoticeBrief> workerEmails = mapper.getWorkerEmails(id);
        return converter.toUserNoticeList(workerEmails);
    }

    @Override
    public List<UserNoticeBriefVO> getRequesterNoticeList(int id) {
        List<UserNoticeBrief> requesterEmails = mapper.getRequesterEmails(id);
        return converter.toUserNoticeList(requesterEmails);
    }

    @Override
    public List<NoticeVO> getAdminNoticeList(int auth) {
        List<Notice> notices;
        switch (auth) {
            case 0: notices = mapper.getAll(); break;
            case 1: notices = mapper.getByType(1); break;
            case 2: notices = mapper.getByType(2); break;
            default: throw new AuthorityException(auth);
        }
        return converter.toNoticeList(notices);
    }

    @Override
    public void addWorkerNoticeRead(int workerId, int noticeId) {
        if(! mapper.checkWorkerRead(workerId, noticeId)) {
            mapper.addWorkerReadRecord(workerId, noticeId);
        }
    }

    @Override
    public void addRequesterNoticeRead(int requesterId, int noticeId) {
        if(! mapper.checkRequesterRead(requesterId, noticeId)) {
            mapper.addRequesterReadRecord(requesterId, noticeId);
        }
    }

    public class AuthorityException extends RuntimeException {
        public AuthorityException(int auth) {
            super("Authority number is incorrect: " + auth);
        }
    }

}
