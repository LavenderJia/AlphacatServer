package com.alphacat.notice;

import com.alphacat.pojo.Notice;
import com.alphacat.pojo.UserNoticeBrief;
import com.alphacat.vo.NoticeDetailVO;
import com.alphacat.vo.NoticeVO;
import com.alphacat.vo.UserNoticeBriefVO;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NoticeConverter {

    @Autowired
    private Mapper mapper;

    public static final String FOREVER = "9999-12-31 23:59:59";

    public List<UserNoticeBriefVO> toUserNoticeList(List<UserNoticeBrief> noticeBriefs) {
        return noticeBriefs.stream().map(this::toVO).collect(Collectors.toList());
    }

    public List<NoticeVO> toNoticeList(List<Notice> notices) {
        return notices.stream().map(this::toVO).collect(Collectors.toList());
    }

    public Notice toPOJO(NoticeVO noticeVO) {
        return mapper.map(noticeVO, Notice.class);
    }

    public NoticeDetailVO toDetailVO(Notice notice) {
        return mapper.map(notice, NoticeDetailVO.class);
    }

    private NoticeVO toVO(Notice notice) {
        NoticeVO result = mapper.map(notice, NoticeVO.class);
        if(result.getEndDate().equals(FOREVER)) {
            result.setEndDate("永久");
        }
        return result;
    }

    private UserNoticeBriefVO toVO(UserNoticeBrief email) {
        return mapper.map(email, UserNoticeBriefVO.class);
    }

}
