package com.alphacat.notice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alphacat.mapper.NoticeMapper;
import com.alphacat.mapper.RequesterMapper;
import com.alphacat.pojo.Notice;
import com.alphacat.pojo.Requester;
import com.alphacat.vo.NoticeDetailVO;
import com.alphacat.vo.NoticeVO;
import com.alphacat.vo.RequesterVO;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NoticeControllerTest {

    @Autowired
    private NoticeController noticeController;
    @Autowired
    private NoticeConverter noticeConverter;
    @Autowired
    private NoticeMapper noticeMapper;
    private MockMvc mvc;
    private RequestBuilder request = null;

    SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String start = tempDate.format(new java.util.Date());

    private NoticeVO noticeVO0 = new NoticeVO(0, "2018-05-30 19:36:35.00", "2018-05-30 19:36:35.00", "AlphaCat项目即将上线", "这是你从未体验的船新版本，快来加入我们，只需体验三分钟，你介会爱上这个平台", 0);
    private NoticeVO noticeVO1 = new NoticeVO(1, "2018-05-26 21:04:56.80", "2018-07-20 00:00:00.00", "a", "aaa", 0);
    private NoticeVO noticeVO2 = new NoticeVO(2, "2018-05-27 08:15:50.80", "2018-05-27 18:00:00.00", "b", "bbb",1);
    private NoticeVO noticeVO3 = new NoticeVO(3, start, "2018-07-22 01:00:00", "c", "ccc", 0);
    private NoticeVO noticeVO4 = new NoticeVO(4, "2018-05-20 13:14:00.00", "2018-06-20 17:27:00.33", "d", "ddd", 1);
    private NoticeVO noticeVO5 = new NoticeVO(5, "2020-06-07 08:00:00.00", "2020-06-09 16:30:00.00", "e", "eee", 2);

    private NoticeDetailVO nd2 = new NoticeDetailVO(2, "b", "bbb","2018-05-27 08:15:50");

    @Before
    public void setUp(){
        mvc = MockMvcBuilders.standaloneSetup(noticeController).build();
    }

    @Test
    public void A_addNotice() throws Exception {
    //    testSingleAdd(noticeVO3);
      //  testSingleAdd(noticeVO4);
        //testSingleAdd(noticeVO5);

    }

    /**
     * A test for add a single notice account
     */
   /* private void testSingleAdd(NoticeVO notice) throws Exception {
        int id = notice.getId();
        JSONObject r = (JSONObject) JSON.parse(JSON.toJSONString(notice));
   //     r.fluentRemove("id").fluentRemove("startDate").fluentRemove("type");
        request = post("/notice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(r.toJSONString());
        mvc.perform(request).andExpect(status().isOk());
        // test for its existence
        Notice actual = noticeMapper.get(id);
        Notice expected = noticeConverter.toPOJO(notice);
        assertEquals(expected, actual);
    }

    @Test
    public void B_get(){
        request = get("/notice/2");
        try {
            mvc.perform(request).andExpect(status().isOk())
                    .andExpect(content().json(JSON.toJSONString(nd2)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void C_addReadRecord(){
        //todo:涉及到用户的登录模块，暂时不会测
    }

    @Test
    public void D_getNoticeList(){
        //todo:涉及到用户的登录模块，暂时不会测
    }

    @Test
    public void Z_testEnd() {
        //迫不得已直接调用mapper收尾
    //    noticeMapper.delete(3);
        request = delete("/notice/3");
        try {
            mvc.perform(request).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }

        noticeMapper.delete(4);
        noticeMapper.delete(5);

    }*/
}