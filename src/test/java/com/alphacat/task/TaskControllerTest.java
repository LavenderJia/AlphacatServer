package com.alphacat.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alphacat.mapper.TaskMapper;
import com.alphacat.mapper.LabelMapper;
import com.alphacat.pojo.Requester;
import com.alphacat.pojo.Task;
import com.alphacat.vo.RequesterVO;
import com.alphacat.vo.TaskVO;
import com.alphacat.vo.LabelVO;
//import com.alphacat.vo.W_TaskVO;
//import com.alphacat.vo.R_TaskVO;
import com.alphacat.vo.HistoryTaskVO;
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
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TaskControllerTest {
    @Autowired
    private TaskController taskController;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskConverter taskConverter;
    @Autowired
    private LabelMapper labelMapper;
    private MockMvc mvc;
    private RequestBuilder request=null;

    private ArrayList<String> choicesB1=new ArrayList<>();
    private ArrayList<String> choicesB2=new ArrayList<>();
    private ArrayList<String> choicesD1=new ArrayList<>();
    private ArrayList<String> choicesD2=new ArrayList<>();

    private LabelVO labelB1=new LabelVO("l21", choicesB1);
    private LabelVO labelB2=new LabelVO("l22", choicesB2);
    private LabelVO labelD1=new LabelVO("l41", choicesD1);
    private LabelVO labelD2=new LabelVO("l42", choicesD1);

    private List<LabelVO> labelB = new ArrayList<LabelVO>( );
    private List<LabelVO> labelD = new ArrayList<LabelVO>( );

    TaskVO taskA=new TaskVO(1,1,"t11","aaa",1,10,2,false,"2018-04-27","2018-05-01",null);
    TaskVO taskB=new TaskVO(2,1,"t12","bbb",2,20,1,true,"2018-04-01","2018-04-10", labelB);
    TaskVO taskC=new TaskVO(3,2,"t23","ccc",3,30,3,false,"2018-04-29","2018-05-10",null);
    TaskVO taskD=new TaskVO(4,2,"t24","ddd",4,40,1,false,"2018-04-20","2018-05-20",labelD);
    TaskVO taskE=new TaskVO(5,1,"t15","eee",5,50,2,true,"2018-04-10","2018-04-27",null);


    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(taskController).build();
        choicesB1.add("c211");
        choicesB1.add("c212");
        choicesB1.add("c213");
        choicesB2.add("c221");
        choicesB2.add("c222");
        choicesB2.add("c223");
        choicesD1.add("c411");
        choicesD1.add("c412");
        choicesD2.add("c421");
        choicesD2.add("c422");

    }

    @Test
    public void A_addTask() throws Exception {
        testSingleAdd(taskD);
        testSingleAdd(taskE);
    }

    /**
     * A test for add a single task account with its password.
     */
    private void testSingleAdd(TaskVO task) throws Exception {
        JSONObject r = (JSONObject) JSON.parse(JSON.toJSONString(task));
        r.fluentRemove("label");
        request = post("/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(r.toJSONString());
        mvc.perform(request).andExpect(status().isOk());
        // test for its existence
        Task actual = taskMapper.get(4);
        Task expected = taskConverter.toPOJO(task);
        assertEquals(expected, actual);
    }

/*    @Test
    public void B_updateTask() throws Exception{
        LabelVO[] labelsD=new LabelVO[]{labelD1,labelD2};
        taskD.setLabels(labelsD);
        request = post("/task/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject.toJSONString(taskD));
        mvc.perform(request).andExpect(status().isOk());
    }

    @Test
    public void C_getPublishedTasks() throws Exception{
        List<R_TaskVO> result=new LinkedList<>();
        result.add(new R_TaskVO(1,"t11","2018-04-27","2018-05-01",1,0,0.0));
        result.add(new R_TaskVO(5,"t15","2018-04-10","2018-04-27",1,0,0.0));
        request = post("/task/published")
                .param("requesterId","1");
        mvc.perform(request).andExpect(content().json(JSONObject.toJSON(result).toString()));
    }

    @Test
    public void C_getFinishedTasks() throws Exception{
        List<R_TaskVO> result=new LinkedList<>();
        result.add(new R_TaskVO(2,"t12","2018-04-01","2018-04-10",2,3,1.2));
        request = post("/task/finished")
                .param("requesterId","1");
        mvc.perform(request).andExpect(content().json(JSONObject.toJSON(result).toString()));
    }

    @Test
    public void C_getAvailableTasks() throws Exception{
        List<W_TaskVO> result=new LinkedList<>();
        result.add(new W_TaskVO(1,"t11","2018-05-01",0,0,1,10));
        result.add(new W_TaskVO(4,"t24","2018-05-20",0,0,4,40));
        result.add(new W_TaskVO(5,"t15","2018-04-27",0,0,5,50));
        request = post("/task/available")
                .param("workerId","1");
        mvc.perform(request).andExpect(content().json(JSONObject.toJSON(result).toString()));
    }

    @Test
    public void C_getHistoryTasks() throws Exception{
        List<HistoryTaskVO> result=new LinkedList<>();
        result.add(new HistoryTaskVO(2,"t12","2018-04-01","2018-04-10",4));
        request = post("/task/history")
                .param("workerId","2");
        mvc.perform(request).andExpect(content().json(JSONObject.toJSON(result).toString()));
    }*/

    @Test
    public void D_deleteTask() {
        taskMapper.delete(4);
        taskMapper.delete(5);
        labelMapper.delete(2);
    }
}