package com.alphacat.mapper;

import com.alphacat.pojo.Requester;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RequesterMapperTest {

    @Autowired
    private RequesterMapper requesterMapper;
    // used to record the data in database
    private static List<Requester> requesters;

    @BeforeClass
    public static void setUp() {
        requesters = new ArrayList<>();
        // This requester data should be the same with those of database.
        requesters.add(new Requester(1, "r1", Date.valueOf("1996-01-01"),
                1, "aaa", "aaa", "aaa", 0));
        requesters.add(new Requester(2, "r2", Date.valueOf("1996-01-02"),
                0, "bbb", "bbb", "bbb", 0));
        requesters.add(new Requester(3, "r3", Date.valueOf("1996-01-03"),
                1, "ccc", "ccc", "ccc", 0));
    }

    @Test
    public void A_get()  {
        Requester r1 = requesters.get(0);
        Requester actual = requesterMapper.get(r1.getId());
        assertEquals(r1, actual);
    }

    @Test
    public void B_addRequester() {
        Date date = Date.valueOf("1995-11-18");
        Requester r4 = new Requester(4, "r4",date,1,"sss",
                "aaa","ddd",0);
        Requester r5 = new Requester(5, "r5",date,1,"sss",
                "abc","sck",1);
        requesters.add(r4);
        requesters.add(r5);
        requesterMapper.add(r4);
        requesterMapper.add(r5);
        assertEquals(r4, requesterMapper.get(r4.getId()));
        assertEquals(r5, requesterMapper.get(r5.getId()));
    }

    @Test
    public void B_checkName() {
        assertTrue(requesterMapper.checkName("r2"));
        assertFalse(requesterMapper.checkName("rrr"));
    }

    @Test
    public void B_checkPwd() {
        assertTrue(requesterMapper.checkPwd("r1", "1234"));
        assertFalse(requesterMapper.checkPwd("r2", "1234"));
    }

    @Test
    public void B_getAll() {
        List<Requester> result = requesterMapper.getAll();
        assertTrue(requesters.containsAll(result));
        assertTrue(result.containsAll(requesters));
    }

    @Test
    public void B_getCheckedRequester() {
        List<Requester> checked = requesters.stream().filter(e -> e.getState() == 0)
                                    .collect(Collectors.toList());
        List<Requester> result = requesterMapper.getByState(0);
//        assertTrue(checked.containsAll(result));
        assertTrue(result.containsAll(checked));
    }

    @Test
    public void B_getNewId() {
        assertEquals(new Integer(6), requesterMapper.getNewId());
    }

    @Test
    public void B_getRequesterByName() {
        assertEquals(requesters.get(3), requesterMapper.getByName("r4"));
    }

    @Test
    public void B_getUncheckedRequester() {
        List<Requester> unchecked = requesters.stream().filter(e -> e.getState() == 1)
                                    .collect(Collectors.toList());
        List<Requester> result = requesterMapper.getByState(1);
        assertTrue(unchecked.containsAll(result));
        assertTrue(result.containsAll(unchecked));
    }

    @Test
    public void C_checkRequester() {
        requesterMapper.activate(5);
        Requester r5 = requesters.get(4);
        r5.setState(0);
        assertEquals(0, requesterMapper.get(5).getState());
    }

    @Test
    public void D_updateRequester() {
        Date date = Date.valueOf("1996-11-18");
        Requester r5 = requesters.get(4);
        r5.setBirth(date);
        r5.setEmail("sssss");
        requesterMapper.update(r5);
        assertEquals(r5, requesterMapper.get(5));
    }

    @Test
    public void E_setPassword() {
        String name = "r2", pwd = "2345";
        requesterMapper.setPwd(name, pwd);
        assertTrue(requesterMapper.checkPwd(name, pwd));
        requesterMapper.setPwd(name, "2222");
    }

    @Test
    public void F_deleteRequester() {
        requesters.remove(4);
        requesters.remove(3);
        requesterMapper.delete(4);
        requesterMapper.delete(5);
        List<Requester> result = requesterMapper.getAll();
        assertTrue(requesters.containsAll(result));
        assertTrue(result.containsAll(requesters));
    }



}