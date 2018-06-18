package com.alphacat.user.avatar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alphacat.mapper.AvatarMapper;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AvatarControllerTest {

    @Autowired
    private AvatarController avatarController;
    @Autowired
    private AvatarMapper avatarMapper;
    private MockMvc mvc;
    private RequestBuilder request = null;

    private WorkerVO workerA = new WorkerVO(1, "w1", "1996-01-01", 1, "www1", "www", 0, 10, 0);


    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(avatarController).build();
    }

    @Test
    public void A_upload() throws Exception{
        MockMultipartFile mockMultipartFile = new MockMultipartFile("07.jpg", new FileInputStream(new File("F:\\FIRED\\渡辺麻友\\07.jpg")));
        JSONObject r = new JSONObject();
        r.fluentPut("file", mockMultipartFile).fluentPut("name", "van").fluentPut("type", "requester");

        request = post("/avatar")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(r.toJSONString());
        mvc.perform(request).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void B_get(){

    }
}