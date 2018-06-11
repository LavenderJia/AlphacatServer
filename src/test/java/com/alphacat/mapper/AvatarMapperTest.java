package com.alphacat.mapper;

import com.alphacat.pojo.Avatar;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AvatarMapperTest {

    @Autowired
    AvatarMapper avatarMapper;

    @Test
    public void A_test_add() {
        byte[] test1 = {1,1,1,1,1,1,1,1};
        avatarMapper.add(new Avatar("van", 0, test1));
        avatarMapper.add(new Avatar("billy", 1,test1));
        avatarMapper.add(new Avatar("banana", 2,test1));
        Assert.assertEquals("van", avatarMapper.get("van",0).getName());
        Assert.assertEquals(test1.toString(), avatarMapper.get("billy",1).getBlob().toString());
        Assert.assertEquals(2, avatarMapper.get("banana",2).getType());


    }

    @Test
    public void B_test_update() {
        byte[] test2 = {1,1,1,1,1,1,1,1,1};
        avatarMapper.update(new Avatar("van", 0, test2));
        Assert.assertEquals("van", avatarMapper.get("van",0).getName());
        Assert.assertEquals(test2.toString(), avatarMapper.get("van",0).getBlob().toString());
        Assert.assertEquals(0, avatarMapper.get("van",0).getType());
    }

    @Test
    public void G_test_delete() {
        avatarMapper.delete("van",0);
        avatarMapper.delete("billy",1);
        avatarMapper.delete("banana",2);
    }
}