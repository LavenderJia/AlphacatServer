package com.alphacat.mapper;

import com.alphacat.pojo.Admin;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdminMapperTest {

    @Autowired
    AdminMapper adminMapper;

    @Test
    public void A_test_add() {
        Date date = Date.valueOf("1985-01-01");
        adminMapper.add(new Admin(1, "a1", "van", 1, 1));
        adminMapper.add(new Admin(2, "a2", "billy", 1, 2));
        Assert.assertEquals(2, adminMapper.getAll().size());
    }

    @Test
    public void B_test_get() {
        Assert.assertEquals("a1", adminMapper.get(1).getName());
    }

    @Test
    public void C_test_getByName() {
        Assert.assertEquals(1, adminMapper.getByName("a1").getSex());
    }

    @Test
    public void D_test_getByAuth() {
        List<Admin> admins = adminMapper.getByAuth(1);
        Assert.assertEquals("a1", admins.get(0).getName());
    }

    @Test
    public void E_test_getAll() {
        List<Admin> admins = adminMapper.getAll();
        Assert.assertEquals("van", admins.get(0).getActualName());
        Assert.assertEquals(2, admins.size());
    }

    @Test
    public void F_test_update() {
        adminMapper.setAuth(1,2);
        adminMapper.setPwd("a1","a1");
        adminMapper.update(new Admin(2, "a2", "banana", 0, 2));
        Assert.assertEquals(2, adminMapper.getByName("a1").getAuth());
        Assert.assertTrue(adminMapper.checkPwd("a1", "a1"));
        Assert.assertEquals("banana", adminMapper.getByName("a2").getActualName());
    }

    @Test
    public void G_test_delete() {
        adminMapper.delete(1);
        adminMapper.delete(2);
        Assert.assertEquals(0,adminMapper.getAll().size());
    }
}