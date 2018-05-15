package com.alphacat.mapper;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DailyRegisterMapperTest {
    @Autowired
    DailyRegisterMapper dailyRegisterMapper;
    @Test
    public void A_addRecord() {
        dailyRegisterMapper.addRecord(1);
        dailyRegisterMapper.addRecord(2);
    }

    @Test
    public void B_hasRecord() {
        Assert.assertTrue(dailyRegisterMapper.hasRecord(1, "2018-5-15"));
        Assert.assertFalse(dailyRegisterMapper.hasRecord(3, "2018-5-15"));
    }

}