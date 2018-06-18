package com.alphacat.mapper;

import com.alphacat.pojo.Task;
import com.alphacat.pojo.TaskRecord;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TaskRecordMapperTest {
    @Autowired
    TaskMapper taskMapper;
    @Autowired
    TaskRecordMapper taskRecordMapper;
    @Test
    public void A_add() {
        Date s4=Date.valueOf("2018-03-29");
        Date e4=Date.valueOf("2018-04-15");
        Date s5=Date.valueOf("2018-06-15");
        Date e5=Date.valueOf("2018-06-27");
        taskMapper.add(new Task(123,3,"t4","ttt",2,30,2,true,s4,e4));
        taskMapper.add(new Task(456,3,"t5","ttt",2,30,2,true,s5,e5));

        taskRecordMapper.add(new TaskRecord(1,123,"1234567",5));
        taskRecordMapper.add(new TaskRecord(1,456,"7654321",3));
    }

    @Test
    public void B_getByWorker() {
        Assert.assertEquals(2,taskRecordMapper.getByWorker(1).size());
        Assert.assertEquals(123,taskRecordMapper.getByWorker(1).get(0).getTaskId());
        Assert.assertEquals("7654321",taskRecordMapper.getByWorker(1).get(1).getPicOrder());
    }

    @Test
    public void C_get() {
        Assert.assertTrue("1234567".equals(taskRecordMapper.get(1,123).getPicOrder()));
    }

    @Test
    public void D_incPicDoneNum() {
        taskRecordMapper.incPicDoneNum(1,123);
        Assert.assertEquals(6, taskRecordMapper.get(1, 123).getPicDoneNum());
    }

    @Test
    public void E_decPicDoneNum() {
        taskRecordMapper.decPicDoneNum(1,123);
        Assert.assertEquals(5, taskRecordMapper.get(1, 123).getPicDoneNum());

    }

    @Test
    public void F_getTaskIds() {
        Assert.assertEquals(123, taskRecordMapper.getTaskIds(1)[0]);
        Assert.assertEquals(456, taskRecordMapper.getTaskIds(1)[1]);
    }

    @Test
    public void G_getWorkerNum(){
        Assert.assertEquals(1, taskRecordMapper.getWorkerNum(123).intValue());
    }

    @Test
    public void H_getTagSum(){
        Assert.assertEquals(5, taskRecordMapper.getTagSum(123).intValue());
    }

    @Test
    public void I_deleteRecord(){
        taskMapper.delete(123);
        taskMapper.delete(456);
    }
}