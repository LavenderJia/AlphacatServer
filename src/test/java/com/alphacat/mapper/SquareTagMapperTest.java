package com.alphacat.mapper;

import com.alphacat.pojo.SquareTag;
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
public class SquareTagMapperTest {
    @Autowired
    SquareTagMapper squareTagMapper;

    @Test
    public void A_add() {
        squareTagMapper.add(new SquareTag(1,1,1,0,1,2,3,4,"{\"st1\":\"ss\",\"st2\":\"ss1\"}","stst"));
        squareTagMapper.add(new SquareTag(1,1,1,1,2,3,4,5,"{\"st1\":\"ss\",\"st2\":\"ss1\"}","stst"));
        squareTagMapper.add(new SquareTag(2,2,1,0,1,2,3,4,"{\"st1\":\"ss\",\"st2\":\"ss1\"}","stst"));
    }

    @Test
    public void B_get() {
        Assert.assertEquals(2,squareTagMapper.get(1,1,1).size());
        Assert.assertEquals(3,squareTagMapper.get(1,1,1).get(1).getY());
        Assert.assertEquals(3,squareTagMapper.get(1,1,1).get(0).getH());

    }

    @Test
    public void C_getAll() {
        Assert.assertEquals(2,squareTagMapper.getAll(1,1).length);
        Assert.assertEquals(3,squareTagMapper.getAll(1,1)[0].getH());
        Assert.assertEquals(4,squareTagMapper.getAll(1,1)[1].getH());
    }

    @Test
    public void D_isExist() {
        Assert.assertTrue(squareTagMapper.isExist(1,1,1));
    }

    @Test
    public void E_update() {
        squareTagMapper.update(new SquareTag(2,2,1,0,4,5,6,7,"{\"st1\":\"ss3\",\"st2\":\"ss1\"}","stst"));
        Assert.assertEquals(4,squareTagMapper.get(2, 2, 1).get(0).getX());
    }

    @Test
    public void F_delete(){
        squareTagMapper.delete(1, 1, 1);
        squareTagMapper.delete(2, 2, 1);
    }
}