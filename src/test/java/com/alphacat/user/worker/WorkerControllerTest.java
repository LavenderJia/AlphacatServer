package com.alphacat.user.worker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alphacat.mapper.WorkerMapper;
import com.alphacat.pojo.Requester;
import com.alphacat.pojo.Worker;
import com.alphacat.vo.RequesterVO;
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

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WorkerControllerTest {

    @Autowired
    private WorkerController workerController;
    @Autowired
    private WorkerConverter workerConverter;
    @Autowired
    private WorkerMapper workerMapper;
    private MockMvc mvc;
    private RequestBuilder request = null;
    private WorkerVO workerA = new WorkerVO(1, "w1", "1996-01-01", 1, "www1", "www", 0, 10, 0);
    private WorkerVO workerB = new WorkerVO(2, "w2", "1996-01-02", 0, "www2", "www", 20, 20, 0);
    private WorkerVO workerC = new WorkerVO(3, "w3", "1996-01-03", 1, "www3", "www", 30, 30, 0);
	private WorkerVO workerD = new WorkerVO(4, "w4", "1993-06-06", 1, "huaqhuaq", "www", 10, 0,0);
    private WorkerVO workerE = new WorkerVO(5, "w5", "1996-10-12", 0, "www5", "www", 0, 0, 2);
    private WorkerVO workerF = new WorkerVO(6, "w6", "1996-03-03", 1, "www6", "www", 0, 0, 2);


    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(workerController).build();
    }

    @Test
    public void A_addWorker() throws Exception {
     //   testSingleAdd(workerE, "55555");
     //   testSingleAdd(workerF, "2333");

    }

    /**
     * A test for add a single requester account with its password.
     */
  /*  private void testSingleAdd(WorkerVO worker, String pwd) throws Exception {
        String name = worker.getName();
        JSONObject w = (JSONObject) JSON.parse(JSON.toJSONString(worker));
        w.fluentRemove("id").fluentPut("key", pwd).fluentPut("state", 2);
        request = post("/worker")
                .contentType(MediaType.APPLICATION_JSON)
                .content(w.toJSONString());
        mvc.perform(request).andExpect(status().isOk());
        // test for its existence
        Worker actual = workerMapper.getByName(name);
        Worker expected = workerConverter.toPOJO(worker);
        assertEquals(expected, actual);
        assertTrue(workerMapper.checkPwd(name, pwd));
    }

    @Test
    public void B_lockWorker() throws Exception {
        request = post("/worker/lock/5")
                .param("id", "5");
        mvc.perform(request).andExpect(status().isOk());
        assertEquals(1, workerMapper.get(5).getState());
    }

    @Test
    public void C_getNormalWorkers()  throws Exception{
        JSONArray ws = new JSONArray();
        JSONObject w = (JSONObject) JSON.parse(JSON.toJSONString(workerA));
        w.fluentRemove("state");
        ws.add(w);
        w = (JSONObject) JSON.parse(JSON.toJSONString(workerB));
        w.fluentRemove("state");
        ws.add(w);
        w = (JSONObject) JSON.parse(JSON.toJSONString(workerC));
        w.fluentRemove("state");
        ws.add(w);
        w = (JSONObject) JSON.parse(JSON.toJSONString(workerD));
        w.fluentRemove("state");
        ws.add(w);
  //      c = (JSONObject) JSON.parse(JSON.toJSONString(workerF));
   //     c.fluentRemove("state");
  //      ws.add(c);
        request = get("/worker?type=active");
        mvc.perform(request).andExpect(status().isOk())
                .andExpect(content().json(ws.toJSONString()));
    }

    @Test
    public void C_getBannedWorkers() throws Exception{
        JSONArray rs = new JSONArray();
        JSONObject r = (JSONObject) JSON.parse(JSON.toJSONString(workerE));
        r.fluentRemove("state");
        rs.add(r);
        request = get("/worker?type=locked");
        mvc.perform(request).andExpect(status().isOk())
                .andExpect(content().json(rs.toJSONString()));
    }

    @Test
    public void C_getWorker() throws Exception{
        WorkerVO worker6 = new WorkerVO(6, "w6", "1996-03-03", 1, "www6", "www", 0, 0, 2);
        request = get("/worker/6");
        mvc.perform(request).andExpect(status().isOk())
                .andExpect(content().json(JSON.toJSONString(worker6)));
    }

    @Test
    public void D_unlockWorker() throws Exception {
        request = post("/worker/unlock/5")
                .param("id", "5");
        mvc.perform(request).andExpect(status().isOk());
        request = post("/worker/unlock/6")
                .param("id", "6");
        mvc.perform(request).andExpect(status().isOk());
        assertEquals(0, workerMapper.get(5).getState());
    }

    @Test
    public void E_updateWorker() throws Exception {
        WorkerVO worker5 = new WorkerVO(5, "w5", "1993-06-06", 1, "huaqhuaq", "www", 0, 0,0);

/*        //测过了，成功提示“该工人账户已被管理员封禁，无法更新。”
        JSONObject c = (JSONObject) JSON.parse(JSON.toJSONString(worker5));
        c.fluentPut("key", "four").fluentRemove("state").fluentRemove("id");
        request = put("/worker/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(c.toJSONString());
        mvc.perform(request).andExpect(status().isOk());
        // check for its function
        Worker actual1 = workerMapper.get(4);
        Worker expected1 = workerConverter.toPOJO(worker5);
        assertEquals(expected1, actual1);
        assertTrue(workerMapper.checkPwd("w5", "four"));
*/
/*        JSONObject w = (JSONObject) JSON.parse(JSON.toJSONString(worker5));
        w.fluentPut("key", "four").fluentRemove("state").fluentRemove("id");
        request = put("/worker/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(w.toJSONString());
        mvc.perform(request).andExpect(status().isOk());
        // check for its function
        Worker actual2 = workerMapper.get(5);
        Worker expected2 = workerConverter.toPOJO(worker5);
        assertEquals(expected2, actual2);
        assertTrue(workerMapper.checkPwd("w5", "four"));
    }

    @Test
    public void E_setPassword() throws Exception {
        request = post("/worker/w1/setPassword")
                .param("password", "11");
        mvc.perform(request).andExpect(status().isOk());
    }

    @Test
    public void E_checkName() throws Exception {
        request = post("/worker/checkName").param("name", "w2");
        mvc.perform(request).andExpect(content().string(equalTo("true")));
        request = post("/worker/checkName").param("name", "w7");
        mvc.perform(request).andExpect(content().string(equalTo("false")));
    }

    @Test
    public void F_signUp()throws Exception{
        request = post("/worker/5/signup");
        mvc.perform(request).andExpect(status().isOk());
        assertEquals(10,workerMapper.get(5).getExp());
    }

    @Test
    public void G_getSignUpInfo()throws Exception{
        request = get("/worker/5/signup");
        mvc.perform(request).andExpect(status().isOk());
        //todo
    }


    @Test
    public void H_getSortedWorkers() throws Exception{
        request = post("/worker/lock/5")
                .param("id", "5");
        mvc.perform(request).andExpect(status().isOk());
        request = get("/worker/data/rank").param("sorted", "exp");
        mvc.perform(request).andExpect(status().isOk())
        .andDo(print());
        request = get("/worker/data/rank").param("sorted", "credit");
        mvc.perform(request).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void H_getRank() throws Exception{
        request = get("/worker/4/rankData");
        mvc.perform(request).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void Z_testEnd() {
        workerMapper.delete(5);
        workerMapper.delete(6);
    }*/
}