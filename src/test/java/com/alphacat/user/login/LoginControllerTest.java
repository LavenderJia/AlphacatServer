package com.alphacat.user.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alphacat.pojo.Admin;
import com.alphacat.vo.AdminVO;
import com.alphacat.vo.WorkerVO;
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

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginControllerTest {

    @Autowired
    private LoginController loginController;
    private MockMvc mvc;
    private RequestBuilder request = null;

    private WorkerVO w1 = new WorkerVO(1, "w1", "1996-01-01", 1, "www1", "www", 0, 10, 0);

    @Before
    public void setUp(){
        mvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    public void A_workerLogin() throws Exception {
        testSingleLogin(w1.getName(), "11");

    }

    private void testSingleLogin(String name, String pwd) throws Exception {
        JSONObject j = new JSONObject();
        j.fluentPut("name", name).fluentPut("key", pwd).fluentPut("type", "worker");
        request = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(j.toJSONString());
        mvc.perform(request).andExpect(status().isOk())
        .andDo(print());
   /*     // test for its existence
        Admin actual = adminMapper.getByName(name);
        Admin expected = adminConverter.toPOJO(adminVO);
        assertEquals(expected, actual);
        assertTrue(adminMapper.checkPwd(name, pwd));*/
    }
}