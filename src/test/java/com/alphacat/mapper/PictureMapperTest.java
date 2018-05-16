package com.alphacat.mapper;

import com.alphacat.pojo.Picture;
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
public class PictureMapperTest {
    @Autowired
    PictureMapper pictureMapper;

    @Test
    public void A_add() {
        pictureMapper.add(new Picture(1,1));
        pictureMapper.add(new Picture(2,1));
        pictureMapper.add(new Picture(3,1));
        pictureMapper.add(new Picture(1,3));
        pictureMapper.add(new Picture(2,3));
    }

    @Test
    public void B_count() {
        Assert.assertEquals(3,pictureMapper.count(1));
    }

    @Test
    public void C_delete() {
        pictureMapper.delete(1);
        pictureMapper.delete(3);
    }
}