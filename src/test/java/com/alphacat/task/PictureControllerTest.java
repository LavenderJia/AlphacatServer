package com.alphacat.task;

import com.alphacat.mapper.PictureMapper;
import org.junit.Before;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PictureControllerTest {

    @Autowired
    private PictureController pictureController;
    @Autowired
    private PictureMapper pictureMapper;
    private MockMvc mvc;
    private RequestBuilder request = null;


    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(pictureController).build();
    }

    @Test
    public void A_get() throws Exception{
        request = get("/pic/1/1");
        mvc.perform(request).andExpect(status().isOk())
                .andDo(print());
    }

}