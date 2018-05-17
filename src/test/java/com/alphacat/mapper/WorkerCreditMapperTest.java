package com.alphacat.mapper;

import com.alphacat.pojo.Task;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;

import static org.junit.Assert.*;
import com.alphacat.pojo.WorkerCredit;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.sql.Timestamp;
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WorkerCreditMapperTest {
    @Autowired
    WorkerCreditMapper workerCreditMapper;
    @Autowired
    TaskMapper taskMapper;

    @Test
    public void A_add() {
        Date s4=Date.valueOf("2018-03-29");
        Date e4=Date.valueOf("2018-04-15");
        Date s5=Date.valueOf("2018-06-15");
        Date e5=Date.valueOf("2018-06-27");
        taskMapper.add(new Task(1,3,"t4","ttt",2,30,2,true,s4,e4));
        taskMapper.add(new Task(3,3,"t5","ttt",2,30,2,true,s5,e5));

        Timestamp ts1=Timestamp.valueOf("2018-05-17 08:45:00");
        Timestamp ts2=Timestamp.valueOf("2018-05-17 08:46:00");
        workerCreditMapper.add(new WorkerCredit(1,1,5,ts1));
        workerCreditMapper.add(new WorkerCredit(1,3,5,ts2));

    }

    @Test
    public void B_get() {
        Assert.assertEquals(2,workerCreditMapper.get(1).size());
    }

    @Test
    public void C_deleteRecord(){
        taskMapper.delete(1);
        taskMapper.delete(3);
    }
}