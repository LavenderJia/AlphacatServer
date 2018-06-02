package com.alphacat.notice;

import com.alphacat.mapper.NoticeMapper;
import com.alphacat.pojo.Notice;
import com.alphacat.pojo.UserNoticeBrief;
import com.alphacat.vo.NoticeDetailVO;
import com.alphacat.vo.NoticeVO;
import com.alphacat.vo.UserNoticeBriefVO;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NoticeConverterTest {
    @Autowired
    private NoticeConverter noticeConverter;
    private MockMvc mvc;
    private RequestBuilder request = null;

    NoticeVO noticeVO0 = new NoticeVO(0, "2018-05-30 19:36:35.00", "2018-05-30 19:36:35.00", "AlphaCat项目即将上线", "这是你从未体验的船新版本，快来加入我们，只需体验三分钟，你介会爱上这个平台", 0);
    NoticeVO noticeVO1 = new NoticeVO(1, "2018-05-26 21:04:56.80", "2018-07-20 00:00:00.00", "a", "aaa", 0);
    NoticeVO noticeVO2 = new NoticeVO(2, "2018-05-27 08:15:50.80", "2018-05-27 18:00:00.00", "b", "bbb",1);

    Timestamp start0 = Timestamp.valueOf("2018-05-30 19:36:35.00");
    Timestamp end0 = Timestamp.valueOf("2018-05-30 19:36:35.00");
    Timestamp start1 = Timestamp.valueOf("2018-05-26 21:04:56.80");
    Timestamp end1 = Timestamp.valueOf("2018-07-20 00:00:00.00");
    Timestamp start2 = Timestamp.valueOf("2018-05-27 08:15:50.80");
    Timestamp end2 = Timestamp.valueOf("2018-05-27 18:00:00.00");

    Notice notice0 = new Notice(0, "AlphaCat项目即将上线", "这是你从未体验的船新版本，快来加入我们，只需体验三分钟，你介会爱上这个平台", start0, end0, 0);
    Notice notice1 = new Notice(1, "a", "aaa", start1, end1, 0);
    Notice notice2 = new Notice(2, "b", "bbb", start2, end2, 1);
    @Test
    public void toUserNoticeList() {
        List <UserNoticeBriefVO> expected = new ArrayList<>();
        UserNoticeBriefVO u0 = new UserNoticeBriefVO(0, "AlphaCat项目即将上线","2018-05-30 19:36:35.00", 0 );
        UserNoticeBriefVO u1 = new UserNoticeBriefVO(1, "a", "2018-05-26 21:04:56.80", 1);
        UserNoticeBriefVO u2 = new UserNoticeBriefVO(2, "b", "2018-05-27 08:15:50.80",0);
        expected.add(u0);
        expected.add(u1);
        expected.add(u2);

        List<UserNoticeBrief> unb = new ArrayList<>();
        UserNoticeBrief  unb0 = new UserNoticeBrief(0, "AlphaCat项目即将上线", start0, 0);
        UserNoticeBrief  unb1 = new UserNoticeBrief(1, "a", start0, 1);
        UserNoticeBrief  unb2 = new UserNoticeBrief(2, "b", start0, 0);
        unb.add(unb0);
        unb.add(unb1);
        unb.add(unb2);

        List<UserNoticeBriefVO> actual = noticeConverter.toUserNoticeList(unb);
        assertEquals(expected, actual);
    }

    @Test
    public void toNoticeList() {
        List<Notice> notices = new ArrayList<>();
        notices.add(notice0);
        notices.add(notice1);
        notices.add(notice2);

        List<NoticeVO> expected = new ArrayList<>();
        expected.add(noticeVO0);
        expected.add(noticeVO1);
        expected.add(noticeVO2);
        List<NoticeVO> actual = noticeConverter.toNoticeList(notices);

        assertEquals(expected, actual);
    }

    @Test
    public void toPOJO() {
        Notice expected0 = notice0;
        Notice expected1 = notice1;
        Notice expected2 = notice2;

        Notice actual0 = noticeConverter.toPOJO(noticeVO0);
        Notice actual1 = noticeConverter.toPOJO(noticeVO1);
        Notice actual2 = noticeConverter.toPOJO(noticeVO2);

        assertEquals(expected0, actual0);
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    public void toDetailVO() {
        NoticeDetailVO expected0 = new NoticeDetailVO(0, "AlphaCat项目即将上线", "这是你从未体验的船新版本，快来加入我们，只需体验三分钟，你介会爱上这个平台", "2018-05-30 19:36:35.00");
        NoticeDetailVO expected1 = new NoticeDetailVO(1, "a", "aaa", "2018-05-26 21:04:56.80");
        NoticeDetailVO expected2 = new NoticeDetailVO(2, "b", "bbb", "2018-05-27 08:15:50.80");

        NoticeDetailVO actual0 = noticeConverter.toDetailVO(notice0);
        NoticeDetailVO actual1 = noticeConverter.toDetailVO(notice1);
        NoticeDetailVO actual2 = noticeConverter.toDetailVO(notice2);


        assertEquals(expected0, actual0);
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }
}