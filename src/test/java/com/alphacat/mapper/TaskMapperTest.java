package com.alphacat.mapper;

import com.alphacat.pojo.Task;
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
public class TaskMapperTest {
    @Autowired
    TaskMapper taskMapper;

    @Test
    public void A_add() {
        Date s4=Date.valueOf("2018-03-29");
        Date e4=Date.valueOf("2018-04-15");
        Date s5=Date.valueOf("2018-06-15");
        Date e5=Date.valueOf("2018-06-27");
        taskMapper.add(new Task(4,3,"t4","ttt",2,30,2,true,s4,e4));
        taskMapper.add(new Task(5,3,"t5","ttt",2,30,2,true,s5,e5));

    }

    @Test
    public void B_getNewId() {
        Assert.assertEquals("9",taskMapper.getNewId().toString());
    }

    @Test
    public void C_getByRequester() {
        Assert.assertEquals(2,taskMapper.getByRequester(1).size());
    }

    @Test
    public void D_getIdleTasks() {
        Assert.assertEquals(0,taskMapper.getIdleTasks(1).size());
        Assert.assertEquals(1,taskMapper.getIdleTasks(3).size());
    }

    @Test
    public void E_getUnderwayTasks() {
        Assert.assertEquals(1,taskMapper.getUnderwayTasks(1).size());
        Assert.assertEquals(0,taskMapper.getUnderwayTasks(3).size());
    }

    @Test
    public void F_getEndedTasks() {
        Assert.assertEquals(1,taskMapper.getEndedTask(1).size());
        Assert.assertEquals(1,taskMapper.getEndedTask(3).size());
    }

    @Test
    public void G_getAvailableTask() {
        Assert.assertEquals(1,taskMapper.getAvailableTask(1).size());
    }

    @Test
    public void H_getHistoryTasks() {
        Assert.assertEquals(0,taskMapper.getHistoryTasks(1).size());
    }

    @Test
    public void I_get() {
        Assert.assertTrue("t4".equals(taskMapper.getByRequester(3).get(0).getName()));
    }

    @Test
    public void J_update() {
        Date s4=Date.valueOf("2018-03-29");
        Date e4=Date.valueOf("2018-04-15");
        taskMapper.update(new Task(4,3,"t4","TTT",2,30,2,true,s4,e4));
        Assert.assertTrue("TTT".equals(taskMapper.get(4).getDescription()));
    }

    @Test
    public void K_delete() {
        taskMapper.delete(4);
        taskMapper.delete(5);

        Assert.assertEquals(0,taskMapper.getByRequester(3).size());
    }
}