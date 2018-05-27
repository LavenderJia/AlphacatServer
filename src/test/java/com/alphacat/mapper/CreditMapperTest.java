package com.alphacat.mapper;

import com.alphacat.pojo.RequesterCredit;
import com.alphacat.pojo.Task;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;

import com.alphacat.pojo.WorkerCredit;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreditMapperTest {
    @Autowired
    CreditMapper creditMapper;
    @Autowired
    TaskMapper taskMapper;

    private static WorkerCredit credit1, credit2;

    @BeforeClass
    public static void setUp() {
        Date date =Date.valueOf("2018-05-17");
        credit1 = new WorkerCredit(1,1, "t4",5,date, 10);
        date = Date.valueOf("2018-05-27");
        credit2 = new WorkerCredit(1,3,"t5",5,date, 20);
    }

    @Test
    public void A_add() {
        Date s4=Date.valueOf("2018-03-29");
        Date e4=Date.valueOf("2018-04-15");
        Date s5=Date.valueOf("2018-06-15");
        Date e5=Date.valueOf("2018-06-27");
        taskMapper.add(new Task(1,3,"t4","ttt",2,30,2,true,s4,e4));
        taskMapper.add(new Task(3,3,"t5","ttt",2,30,2,true,s5,e5));

        creditMapper.add(credit1);
        creditMapper.add(credit2);
    }

    @Test
    public void B_getW() {
        List<WorkerCredit> credits = creditMapper.getWorkerCredits(1);
        Assert.assertEquals(2, credits.size());
        Assert.assertEquals(credit2, credits.get(0));
        Assert.assertEquals(credit1, credits.get(1));
    }

    @Test
    public void B_getR() {
        List<RequesterCredit> credits = creditMapper.getRequesterCredits(3);
        Assert.assertEquals(2, credits.size());
        RequesterCredit r_credit1 = new RequesterCredit(3, "t5", credit2.getDate(), 5);
        Assert.assertEquals(r_credit1, credits.get(0));
        RequesterCredit r_credit2 = new RequesterCredit(1, "t4", credit1.getDate(), 5);
        Assert.assertEquals(r_credit2, credits.get(1));
    }

    @Test
    public void C_deleteRecord(){
        taskMapper.delete(1);
        taskMapper.delete(3);
    }

}