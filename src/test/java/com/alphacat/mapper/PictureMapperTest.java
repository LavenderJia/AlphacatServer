package com.alphacat.mapper;

import com.alphacat.pojo.Picture;
import com.alphacat.pojo.Task;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PictureMapperTest {
    @Autowired
    PictureMapper pictureMapper;
    @Autowired
    private TaskMapper taskMapper;

    @Test
    public void A_setUp() {
        Date start4=Date.valueOf("2018-03-29");
        Date end4=Date.valueOf("2018-04-15");
        Date start5=Date.valueOf("2018-06-15");
        Date end5=Date.valueOf("2018-06-27");
        taskMapper.add(new Task(4,3,"t4","ttt",2,30,2,true,start4,end4));
        taskMapper.add(new Task(5,3,"t5","ttt",2,30,2,true,start5,end5));
    }

    @Test
    public void B_add() {
        pictureMapper.add(new Picture(1,4));
        pictureMapper.add(new Picture(2,4));
        pictureMapper.add(new Picture(3,5));
        pictureMapper.add(new Picture(1,5));
        pictureMapper.add(new Picture(2,5));
    }

    @Test
    public void C_count() {
        Assert.assertEquals(3,pictureMapper.count(5));
    }

    @Test
    public void D_delete() {
        pictureMapper.multiDelete(4);
        pictureMapper.multiDelete(5);
    }

    @Test
    public void Z_tearDown() {
        taskMapper.delete(4);
        taskMapper.delete(5);
    }
}