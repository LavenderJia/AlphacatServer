package com.alphacat.credit.transaction;

import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreditControllerTest {

    @Autowired
    private CreditController creditController;
    @Autowired
    private  CreditConverter creditConverter;

    private MockMvc mvc;
    private RequestBuilder request = null;
    private MockHttpSession session;

    protected MockMvc getMockMvc()
    {
        return this.mvc;
    }


    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(creditController).build();
        this.session = (MockHttpSession)getLoginSession();
    }

    private HttpSession getLoginSession()
    {
        String url = "/login";
        JSONObject j = new JSONObject();
        j.fluentPut("name", "w1").fluentPut("key", "11").fluentPut("type", "worker");
        MvcResult result = null;
        try
        {
            result = getMockMvc().perform(
                    MockMvcRequestBuilders.post(url).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)  .content(j.toJSONString())).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result.getRequest().getSession();
    }

    @Test
    public void A_getTransactions(){
        request = get("/task");
        try {
            mvc.perform(request).andExpect(status().isOk())
                    .andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}