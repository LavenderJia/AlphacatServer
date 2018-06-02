package com.alphacat.mapper;

import com.alphacat.pojo.Admin;
import com.alphacat.pojo.Notice;
import com.alphacat.pojo.UserNoticeBrief;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NoticeMapperTest {

    @Autowired
    NoticeMapper noticeMapper;

    @Test
    public void A_add() {
        Timestamp start1 = Timestamp.valueOf("2018-05-30 08:00:00.00");
        Timestamp end1 = Timestamp.valueOf("2018-05-30 09:00:00.00");
        Timestamp start2 = Timestamp.valueOf("2018-06-01 00:00:00.00");
        Timestamp end2 = Timestamp.valueOf("2018-06-12 23:59:59.02");
        Timestamp start3 = Timestamp.valueOf("2019-09-09 09:09:09.09");
        Timestamp end3 = Timestamp.valueOf("2020-06-22 01:23:45.67");

        noticeMapper.add(new Notice(3, "n3", "do you like van游戏",start1,end1,  0 ));
        noticeMapper.add(new Notice(4, "n4", "deep dark fantasy",start2,end2,  1 ));
        noticeMapper.add(new Notice(5, "n5", "奥义很爽",start3,end3,  2 ));

        Assert.assertEquals(6, noticeMapper.getAll().size());
        Assert.assertEquals(0, noticeMapper.get(3).getType());
        Assert.assertEquals("deep dark fantasy", noticeMapper.get(4).getContent());
        Assert.assertEquals(start3, noticeMapper.get(5).getStartDate());
    }

    @Test
    public void B_getNewId() {
        Assert.assertEquals("6", noticeMapper.getNewId().toString());
    }

    @Test
    public void C_getAll() {
        Timestamp start1 = Timestamp.valueOf("2018-05-30 08:00:00.00");
        Timestamp end1 = Timestamp.valueOf("2018-05-30 09:00:00.00");
        Timestamp start2 = Timestamp.valueOf("2018-06-01 00:00:00.00");
        Timestamp end2 = Timestamp.valueOf("2018-06-12 23:59:59.02");
        Timestamp start3 = Timestamp.valueOf("2019-09-09 09:09:09.09");
        Timestamp end3 = Timestamp.valueOf("2020-06-22 01:23:45.67");

        Notice notice3 = new Notice(3, "n3", "do you like van游戏",start1,end1,  0 );
        Notice notice4 = new Notice(4, "n4", "deep dark fantasy",start2,end2,  1 );
        Notice notice5 = new Notice(5, "n5", "奥义很爽",start3,end3,  2 );
        Assert.assertEquals(notice3, noticeMapper.getAll().get(3));
        Assert.assertEquals(notice4, noticeMapper.getAll().get(4));
        Assert.assertEquals(notice5, noticeMapper.getAll().get(5));
    }

    @Test
    public void D_getByType(){
        List<Notice> type1 = new ArrayList<>();
        Timestamp start1 = Timestamp.valueOf("2018-05-27 08:15:50.80");
        Timestamp end1 = Timestamp.valueOf("2018-05-27 18:00:00.00");
        Timestamp start2 = Timestamp.valueOf("2018-06-01 00:00:00.00");
        Timestamp end2 = Timestamp.valueOf("2018-06-12 23:59:59.02");
        Notice notice2 = new Notice(2, "b", "bbb",start1,end1,  1 );
        Notice notice4 = new Notice(4, "n4", "deep dark fantasy",start2,end2,  1 );
        type1.add(notice2);
        type1.add(notice4);
        Assert.assertEquals(notice2, noticeMapper.getByType(1).get(2));
        Assert.assertEquals(notice4, noticeMapper.getByType(1).get(4)) ;
    }

    @Test
    public void E_getWorkerEmails(){
        Timestamp start2 = Timestamp.valueOf("2018-05-26 21:04:56.80");
        Timestamp start3 = Timestamp.valueOf("2019-09-09 09:09:09.09");
        UserNoticeBrief u0 = new UserNoticeBrief(1, "a",start2,0 );
        UserNoticeBrief u1 = new UserNoticeBrief(5, "n5",start3,1 );
        Assert.assertEquals(false, noticeMapper.checkWorkerRead(1,5));
        noticeMapper.addWorkerReadRecord(1,5);
        Assert.assertEquals(u0, noticeMapper.getWorkerEmails(1).get(1));
        Assert.assertEquals(u1, noticeMapper.getWorkerEmails(1).get(2));
    }

    @Test
    public void F_getRequesterEmails(){
        Timestamp start1 = Timestamp.valueOf("2018-05-26 21:04:56.80");
        Timestamp start2 = Timestamp.valueOf("2018-06-01 00:00:00.00");
        UserNoticeBrief u0 = new UserNoticeBrief(1, "a",start1,1 );
        UserNoticeBrief u1 = new UserNoticeBrief(4, "n4",start2,1 );
        Assert.assertEquals(true, noticeMapper.checkRequesterRead(1,1));
        Assert.assertEquals(false, noticeMapper.checkRequesterRead(1,4));
        noticeMapper.addRequesterReadRecord(1,4);
        Assert.assertEquals(u0, noticeMapper.getRequesterEmails(1).get(1));
        Assert.assertEquals(u1, noticeMapper.getRequesterEmails(1).get(2));
    }


    @Test
    public void Z_delete() {
        noticeMapper.delete(3);
        noticeMapper.delete(4);
        noticeMapper.delete(5);
        Assert.assertEquals(null, noticeMapper.get(3));
        Assert.assertEquals(3, noticeMapper.getAll().size());
    }
}