package com.alphacat.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alphacat.mapper.TaskMapper;
import com.alphacat.mapper.LabelMapper;
import com.alphacat.pojo.Task;
import com.alphacat.vo.AvailableTaskVO;
import com.alphacat.vo.TaskVO;
import com.alphacat.vo.LabelVO;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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

    TaskVO taskA=new TaskVO(1,0,"测试任务一","新手任务——单框标注测试任务",2,20,1,false,"2018-04-01","2019-04-10",null);
    TaskVO taskB=new TaskVO(2,0,"测试任务二","新手任务——多框标注测试任务",2,20,2,false,"2018-06-01","2019-06-30", labelB);
    TaskVO taskC=new TaskVO(3,0,"测试任务三","新手任务——不规则标注测试任务",4,30,3,false,"2018-05-05","2019-05-16",null);
    TaskVO taskD=new TaskVO(4,2,"t24","ddd",4,40,1,false,"2018-05-30","2018-06-20",labelD);
    TaskVO taskE=new TaskVO(5,1,"t15","eee",5,50,2,true,"2018-05-30","2018-06-27",labelD);


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
        int id = task.getId();
        JSONObject r = (JSONObject) JSON.parse(JSON.toJSONString(task));
        r.fluentRemove("id").fluentPut("state", "draft");
        request = post("/task")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(r.toJSONString());
        mvc.perform(request).andExpect(status().isOk());
        // test for its existence
        Task actual = taskMapper.get(id);
        Task expected = taskConverter.toPOJO(task);
        assertEquals(expected, actual);
    }

    @Test
    public void B_updateTask() throws Exception{
        labelD.add(labelD1);
        labelD.add(labelD2);
        TaskVO newTaskD=new TaskVO(4,2,"t25","ddd",4,40,1,false,"2018-06-19","2018-06-20",labelD);
        JSONObject r = (JSONObject) JSON.parse(JSON.toJSONString(newTaskD));
        request = put("/task")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(r.toJSONString());
        mvc.perform(request).andExpect(status().isOk());
        assertEquals("t25",taskMapper.get(4).getName());
    }

    @Test
    public void C_getIdleTasks() throws Exception{
        AvailableTaskVO idleTaskE = new AvailableTaskVO(10,"t25", 5,50,2, "2018-06-27");
        List<AvailableTaskVO> result=new ArrayList<>();
        result.add(idleTaskE);
        request = get("/task?requesterId=1&type=underway");
        mvc.perform(request).andExpect(status().isOk())
                .andExpect(content().json(JSONObject.toJSON(result).toString()));
    }

 /*   @Test
    public void C_getIdleTasks() throws Exception{
        List<R_TaskVO> result=new LinkedList<>();
        result.add(new R_TaskVO(2,"t12","2018-04-01","2018-04-10",2,3,1.2));
        request = get("/task")
                .param("requesterId","1")
                .param("type", "notstart");
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