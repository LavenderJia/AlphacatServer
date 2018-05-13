package com.alphacat.mapper;

import com.alphacat.pojo.Requester;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.sql.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RequesterMapperTest {

    @Autowired
    @Resource
    private RequesterMapper requesterMapper;

    @Test
    public void A_addRequester() {
        Date date = Date.valueOf("1995-11-18");

        requesterMapper.add(new Requester(4, "r4",date,1,"sss","aaa","ddd",0));
        requesterMapper.add(new Requester(5, "r5",date,1,"sss","aaa","ddd",1));

    }

    @Test
    public void B_getNewId() {
        assertEquals(new Integer(6), requesterMapper.getNewId());
    }

    @Test
    public void B_getAllRequester() {
        assertEquals(5,requesterMapper.getAll().size());
    }

    @Test
    public void B_getRequesterByName() {
        assertEquals(4, requesterMapper.getByName("r4").getId());
    }

    @Test
    public void B_getCheckedRequester() {
        assertEquals(4,requesterMapper.getByState(0).size());
    }

    @Test
    public void B_checkName() {
        assertTrue(requesterMapper.checkName("r2"));
        assertFalse(requesterMapper.checkName("rrr"));
    }

    @Test
    public void B_getUncheckedRequester() {
        assertEquals(1, requesterMapper.getByState(1).size());
    }

    @Test
    public void C_checkRequester() {
        requesterMapper.activate(5);
        assertEquals(0, requesterMapper.getByState(1).size());
    }

    @Test
    public void D_updateRequester() {
        Date date = Date.valueOf("1996-11-18");
        requesterMapper.update(new Requester(5, "r5",date,1,"sssss","aaa","ddd",0));
        assertTrue("sssss".equals(requesterMapper.getByName("r5").getEmail()));
    }

    @Test
    public void E_setPassword() {
        requesterMapper.setPwd("r2", "2222");
    }

    @Test
    public void F_deleteRequester() {
        requesterMapper.delete(4);
        requesterMapper.delete(5);
    }



}