package com.alphacat.mapper;

import com.alphacat.pojo.IrregularTag;
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
public class IrregularTagMapperTest {
    @Autowired
    IrregularTagMapper irregularTagMapper;
    @Autowired
    TaskMapper taskMapper;

    @Test
    public void A_add() {
        Date start3=Date.valueOf("2018-03-29");
        Date end3=Date.valueOf("2018-04-15");

        taskMapper.add(new Task(3,3,"t4","ttt",2,30,2,true,start3,end3));

        irregularTagMapper.add(new IrregularTag(1,3,3,"111111"));
        irregularTagMapper.add(new IrregularTag(1,3,2,"111222"));
        irregularTagMapper.add(new IrregularTag(2,3,3,"222222"));
        irregularTagMapper.add(new IrregularTag(3,3,3,"333333"));
    }

    @Test
    public void B_get() {
        Assert.assertTrue("111111".equals(irregularTagMapper.get(1,3,3).getFigure()));
        Assert.assertTrue("111222".equals(irregularTagMapper.get(1,3,2).getFigure()));
        Assert.assertTrue("222222".equals(irregularTagMapper.get(2,3,3).getFigure()));
        Assert.assertTrue("333333".equals(irregularTagMapper.get(3,3,3).getFigure()));

    }

    @Test
    public void C_getAll() {
        Assert.assertEquals(3,irregularTagMapper.getAll(3,3).size());
        Assert.assertTrue("111111".equals(irregularTagMapper.getAll(3,3).get(0).getFigure()));
        Assert.assertTrue("222222".equals(irregularTagMapper.getAll(3,3).get(1).getFigure()));
        Assert.assertTrue("333333".equals(irregularTagMapper.getAll(3,3).get(2).getFigure()));
    }

    @Test
    public void D_isExist() {
        Assert.assertTrue(irregularTagMapper.isExist(3,3,3));
        Assert.assertFalse(irregularTagMapper.isExist(3,3,1));
    }

    @Test
    public void E_update() {
        irregularTagMapper.update(new IrregularTag(3,3,3,"333444"));
        Assert.assertTrue("333444".equals(irregularTagMapper.get(3,3,3).getFigure()));

    }

    @Test
    public void F_delete() {
     //   irregularTagMapper.delete(1,3,3);
       // irregularTagMapper.delete(1,3,2);
        //irregularTagMapper.delete(2,3,3);
        //irregularTagMapper.delete(3,3,3);
        taskMapper.delete(3);
    }

}