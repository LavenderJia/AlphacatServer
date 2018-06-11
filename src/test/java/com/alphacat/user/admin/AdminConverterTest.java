package com.alphacat.user.admin;

import com.alphacat.pojo.Admin;
import com.alphacat.vo.AdminVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AdminConverterTest {
    @Autowired
    private AdminConverter adminConverter;

    @Test
    public void toVO() {
        Admin admin1 = new Admin(1, "van", "范", 0, 0);
        Admin admin2 = new Admin(2, "billy", "比利海灵顿", 0 ,1);
        Admin admin3 = new Admin(3, "doggy", "saber", 1 ,2);

        AdminVO expected1 = new AdminVO(1, "van", "范", 0, 0);
        AdminVO expected2 = new AdminVO(2, "billy", "比利海灵顿", 0 ,1);
        AdminVO expected3 = new AdminVO(3, "doggy", "saber", 1 ,2);

        AdminVO actual1 = adminConverter.toVO(admin1);
        AdminVO actual2 = adminConverter.toVO(admin2);
        AdminVO actual3 = adminConverter.toVO(admin3);

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
        assertEquals(expected3, actual3);

    }

    @Test
    public void toPOJO() {
        Admin expected1 = new Admin(1, "van", "范", 0, 0);
        Admin expected2 = new Admin(2, "billy", "比利海灵顿", 0 ,1);
        Admin expected3 = new Admin(3, "doggy", "saber", 1 ,2);

        AdminVO adminVO1 = new AdminVO(1, "van", "范", 0, 0);
        AdminVO adminVO2 = new AdminVO(2, "billy", "比利海灵顿", 0 ,1);
        AdminVO adminVO3 = new AdminVO(3, "doggy", "saber", 1 ,2);

        Admin actual1 = adminConverter.toPOJO(adminVO1);
        Admin actual2 = adminConverter.toPOJO(adminVO2);
        Admin actual3 = adminConverter.toPOJO(adminVO3);

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
        assertEquals(expected3, actual3);
    }

    @Test
    public void toVOList() {
        Admin admin1 = new Admin(1, "van", "范", 0, 0);
        Admin admin2 = new Admin(2, "billy", "比利海灵顿", 0 ,1);
        Admin admin3 = new Admin(3, "doggy", "saber", 1 ,2);

        List<Admin> list = new ArrayList<>();
        list.add(admin1);
        list.add(admin2);
        list.add(admin3);

        AdminVO expected1 = new AdminVO(1, "van", "范", 0, 0);
        AdminVO expected2 = new AdminVO(2, "billy", "比利海灵顿", 0 ,1);
        AdminVO expected3 = new AdminVO(3, "doggy", "saber", 1 ,2);

        List<AdminVO> expected = new ArrayList<>();
        expected.add(expected1);
        expected.add(expected2);
        expected.add(expected3);

        List<AdminVO> actual = adminConverter.toVOList(list);
        assertEquals(expected, actual);
    }
}