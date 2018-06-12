package com.alphacat.mapper;

import com.alphacat.pojo.Worker;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WorkerMapperTest {

    @Autowired
    WorkerMapper workerMapper;

    @Test
    public void A_test_add() {
        Date date = Date.valueOf("1985-01-01");
        workerMapper.add(new Worker(5, "w5", date, 1, "www4", "www", 0, 0, 0));
        workerMapper.add(new Worker(6, "w6", date, 1, "www5", "www", 0, 0, 0));
        Assert.assertEquals(6,workerMapper.getAll().size());
    }

    @Test
    public void B_test_getWorkerByName() {
        Assert.assertEquals("www", workerMapper.getByName("w5").getSignature());
    }

    @Test
    public void C_test_getWorkerByState() {
        Assert.assertEquals(6, workerMapper.getByState(0).size());
    }

    @Test
    public void D_test_get() {
        Assert.assertEquals("www", workerMapper.get(1).getSignature());
    }

    @Test
    public void E_test_getNewId() {
        Assert.assertTrue("7".equals(workerMapper.getNewId().toString()));
    }

    @Test
    public void F_test_getAll() {
        Assert.assertEquals(6, workerMapper.getAll().size());
    }

    @Test
    public void G_test_update() {
        Date date=Date.valueOf("1985-01-01");
        workerMapper.addExp(5,3);
        workerMapper.changeState(5,1);
        workerMapper.setPwd("w5","555");
        workerMapper.update(new Worker(6, "w4", date, 1, "www4", "sss", 0, 0, 0));
        Assert.assertEquals(20, workerMapper.getByName("w2").getExp());
        Assert.assertEquals(5, workerMapper.getByState(0).size());
        Assert.assertTrue(workerMapper.checkPwd("w5", "555"));

    }

    @Test
    public void H_test_delete() {
        workerMapper.delete(5);
        workerMapper.delete(6);
        Assert.assertEquals(4,workerMapper.getAll().size());
    }
}