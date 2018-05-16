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
        Date s5=Date.valueOf("2018-04-15");
        Date e5=Date.valueOf("2018-04-27");
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
    public void B_findAvailableTask() {
        Assert.assertEquals(1,taskMapper.getAvailableTask(1).size());
    }

    @Test
    public void B_getTaskById() {
        Assert.assertTrue("t4".equals(taskMapper.getByRequester(3).get(0).getName()));
    }

    @Test
    public void C_updateTask() {
        Date s4=Date.valueOf("2018-03-29");
        Date e4=Date.valueOf("2018-04-15");
        taskMapper.update(new Task(4,3,"t4","TTT",2,30,2,true,s4,e4));
        Assert.assertTrue("TTT".equals(taskMapper.get(4).getDescription()));
    }

    @Test
    public void D_deleteTask() {
        taskMapper.delete(4);
        taskMapper.delete(5);

        Assert.assertEquals(0,taskMapper.getByRequester(3).size());
    }
}