package com.alphacat.user.requester;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alphacat.mapper.RequesterMapper;
import com.alphacat.pojo.Requester;
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

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RequesterControllerTest {

    @Autowired
    private RequesterController requesterController;
    @Autowired
    private RequesterConverter requesterConverter;
    @Autowired
    private RequesterMapper requesterMapper;
    private MockMvc mvc;
    private RequestBuilder request = null;

    private RequesterVO requesterA =
            new RequesterVO(1, "r1","1996-01-01",1,"aaa","aaa","aaa",0);
    private RequesterVO requesterB =
            new RequesterVO(2, "r2","1996-01-02",0,"bbb","bbb","bbb",0);
    private RequesterVO requesterC =
            new RequesterVO(3, "r3","1996-01-03",1,"ccc","ccc","ccc",0);
    private RequesterVO requesterD =
            new RequesterVO(4, "r4","1995-02-01",1,"sss","aaa","ddd",1);
    private RequesterVO requesterE =
            new RequesterVO(5, "r5","1998-02-01",0,"sss","aaa","ddd",1);


    @Before
    public void setUp(){
        mvc = MockMvcBuilders.standaloneSetup(requesterController).build();
    }

    @Test
    public void A_addRequester() throws Exception {
        testSingleAdd(requesterD, "4444");
        testSingleAdd(requesterE, "55555");
    }

    /**
     * A test for add a single requester account with its password.
     */
    private void testSingleAdd(RequesterVO req, String pwd) throws Exception {
        String name = req.getName();
        JSONObject r = (JSONObject) JSON.parse(JSON.toJSONString(req));
        r.fluentRemove("id").fluentPut("key", pwd).fluentPut("state", 1);
        request = post("/requester")
                .contentType(MediaType.APPLICATION_JSON)
                .content(r.toJSONString());
        mvc.perform(request).andExpect(status().isOk());
        // test for its existence
        Requester actual = requesterMapper.getByName(name);
        Requester expected = requesterConverter.toPOJO(req);
        assertEquals(expected, actual);
        assertTrue(requesterMapper.checkPwd(name, pwd));
    }

    @Test
    public void B_getRequester() throws Exception{
        request = get("/requester/1");
        mvc.perform(request).andExpect(status().isOk())
                .andExpect(content().json(JSON.toJSONString(requesterA)));
    }

    @Test
    public void B_getUncheckedRequester() throws Exception {
        JSONArray rs = new JSONArray();
        JSONObject r = (JSONObject) JSON.parse(JSON.toJSONString(requesterD));
        r.fluentRemove("state");
        rs.add(r);
        r = (JSONObject) JSON.parse(JSON.toJSONString(requesterE));
        r.fluentRemove("state");
        rs.add(r);
        request = get("/requester?state=unchecked");
        mvc.perform(request).andExpect(status().isOk())
                .andExpect(content().json(rs.toJSONString()));
    }

    @Test
    public void B_getAllRequester() throws Exception {
        JSONArray rs = new JSONArray();
        JSONObject r = (JSONObject) JSON.parse(JSON.toJSONString(requesterA));
        rs.add(r);
        r = (JSONObject) JSON.parse(JSON.toJSONString(requesterB));
        rs.add(r);
        r = (JSONObject) JSON.parse(JSON.toJSONString(requesterC));
        rs.add(r);
        r = (JSONObject) JSON.parse(JSON.toJSONString(requesterD));
        rs.add(r);
        r = (JSONObject) JSON.parse(JSON.toJSONString(requesterE));
        rs.add(r);
        request = get("/requester?state=all");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(rs.toJSONString()));
    }

    @Test
    public void C_checkName() throws Exception{
        request = post("/requester/checkName").param("name", "r2");
        mvc.perform(request).andExpect(content().string(equalTo("true")));
        request = post("/requester/checkName").param("name", "r6");
        mvc.perform(request).andExpect(content().string(equalTo("false")));
    }

    @Test
    public void D_checkRequester() throws Exception{
        // not pass a requester account
        JSONObject jo = new JSONObject();
        jo.fluentPut("id", 4).fluentPut("isChecked", false);
        request = post("/requester/check")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jo.toJSONString());
        mvc.perform(request).andExpect(status().isOk());
        Requester r = requesterMapper.get(4);
        assertTrue(r == null);
        // pass a requester account
        jo = new JSONObject();
        jo.fluentPut("id", 5).fluentPut("isChecked", true);
        request = post("/requester/check")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jo.toJSONString());
        mvc.perform(request).andExpect(status().isOk());
        r = requesterMapper.get(5);
        assertEquals(0, r.getState());
    }

    @Test
    public void F_updateRequester() throws Exception {
        RequesterVO req = new RequesterVO(5, "r5", "1995-05-12",
                1, "new@ema.il", "hiahia", "rara", 0);
        JSONObject r = (JSONObject) JSON.parse(JSON.toJSONString(req));
        r.fluentPut("key", "new-key").fluentRemove("state").fluentRemove("id");
        request = put("/requester/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(r.toJSONString());
        mvc.perform(request).andExpect(status().isOk());
        // check for its function
        Requester actual = requesterMapper.get(5);
        Requester expected = requesterConverter.toPOJO(req);
        assertEquals(expected, actual);
        assertTrue(requesterMapper.checkPwd("r5", "new-key"));
    }

    @Test
    public void Z_testEnd() {
        //迫不得已直接调用mapper收尾
        requesterMapper.delete(4);
        requesterMapper.delete(5);
    }
}