package com.alphacat.user.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alphacat.mapper.AdminMapper;
import com.alphacat.pojo.Admin;
import com.alphacat.vo.AdminVO;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdminControllerTest {

    @Autowired
    private AdminController adminController;
    @Autowired
    private AdminConverter adminConverter;
    @Autowired
    private AdminMapper adminMapper;
    private MockMvc mvc;
    private RequestBuilder request = null;

    private AdminVO adminVO0 = new AdminVO(0 , "root", "LiuQIn", 1, 0);
    private AdminVO adminVO1 = new AdminVO(1 , "Radmin", "GeJiDong", 1, 1);
    private AdminVO adminVO2 = new AdminVO(2 , "godpi", "pikachu", 0, 2);
    private AdminVO adminVO3 = new AdminVO(3 , "55kai", "lubenwei", 1, 2);



    @Before
    public void setUp(){
        mvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void A_addAdmin() throws Exception {
        testSingleAdd(adminVO2, "4444");
        testSingleAdd(adminVO3, "55555");
        assertTrue(adminMapper.checkPwd("godpi", "4444"));
        assertTrue(adminMapper.checkPwd("55kai", "55555"));

    }

    /**
     * A test for add a single admin account with its password.
     */
    private void testSingleAdd(AdminVO adminVO, String pwd) throws Exception {
        String name = adminVO.getName();
        JSONObject r = (JSONObject) JSON.parse(JSON.toJSONString(adminVO));
        r.fluentPut("key", pwd);
        request = post("/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(r.toJSONString());
        mvc.perform(request).andExpect(status().isOk());
        // test for its existence
        Admin actual = adminMapper.getByName(name);
        Admin expected = adminConverter.toPOJO(adminVO);
        assertEquals(expected, actual);
        assertTrue(adminMapper.checkPwd(name, pwd));
    }

    @Test
    public void B_getRequester() throws Exception{
        request = get("/admin/0");
        mvc.perform(request).andExpect(status().isOk())
                .andExpect(content().json(JSON.toJSONString(adminVO0)));
    }

    @Test
    public void C_getAll() throws Exception{
        List<AdminVO> list = new ArrayList<>();
        list.add(adminVO1);
        list.add(adminVO2);
        list.add(adminVO3);

        request = get("/admin");
        mvc.perform(request).andExpect(status().isOk())
       //         .andDo(print())
                .andExpect(content().json(JSON.toJSONString(list)));
    }

    @Test
    public void D_update(){
        AdminVO adminVO = new AdminVO(3 , "55kai", "lubenwei", 0, 2);
        JSONObject r = (JSONObject) JSON.parse(JSON.toJSONString(adminVO));
        r.fluentPut("key", "12345");
        request = put("/admin/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(r.toJSONString());
        try {
            mvc.perform(request).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }

        request = get("/admin/3");
        try {
            mvc.perform(request).andExpect(status().isOk())
                    .andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // check for its function
        Admin actual = adminMapper.get(3);
        Admin expected = adminConverter.toPOJO(adminVO);
        assertEquals(expected, actual);
        assertTrue(adminMapper.checkPwd("55kai", "12345"));
    }

    @Test
    public void E_setAuth() throws Exception{
        AdminVO adminVO = new AdminVO(3 , "55kai", "lubenwei", 0, 1);
        JSONObject r = (JSONObject) JSON.parse(JSON.toJSONString(adminVO));
        request = put("/admin/3/setAuth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(r.toJSONString());
        try {
            mvc.perform(request).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // check for its function
        Admin actual = adminMapper.get(3);
        Admin expected = adminConverter.toPOJO(adminVO);
        assertEquals(expected, actual);
        assertEquals(1,adminMapper.get(3).getAuth());
    }

    @Test
    public void Z_testEnd() {
        //迫不得已直接调用mapper收尾
       // adminMapper.delete(2);
       // adminMapper.delete(3);
        request = delete("/admin/2")
                .contentType(MediaType.APPLICATION_JSON);
        try {
            mvc.perform(request).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
        request = delete("/admin/3")
                .contentType(MediaType.APPLICATION_JSON);
        try {
            mvc.perform(request).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}