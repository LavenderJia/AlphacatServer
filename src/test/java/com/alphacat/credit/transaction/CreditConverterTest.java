package com.alphacat.credit.transaction;

import com.alphacat.pojo.RequesterCredit;
import com.alphacat.pojo.WorkerCredit;
import com.alphacat.vo.RequesterCreditVO;
import com.alphacat.vo.WorkerCreditVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class CreditConverterTest {

    @Autowired
    private CreditConverter creditConverter;

    private WorkerCredit workerCredit1 = new WorkerCredit(1, 1, "测试任务一", 20, Date.valueOf("2018-06-12"), 25);
    private WorkerCredit workerCredit2 = new WorkerCredit(2, 3, "测试任务三", 20, Date.valueOf("2017-06-12"), 20);

    private WorkerCreditVO workerCreditVO1 = new WorkerCreditVO("2018-06-12", "测试任务一任务结算", 20, 25);
    private WorkerCreditVO workerCreditVO2 = new WorkerCreditVO("2017-06-12", "测试任务三任务结算", 20, 20);

    private RequesterCredit requesterCredit1 = new RequesterCredit(1, "r1", Date.valueOf("2018-06-12"), 200);
    private RequesterCredit requesterCredit2 = new RequesterCredit(2, "r2", Date.valueOf("2017-06-12"), 0);

    private RequesterCreditVO requesterCreditVO1 = new RequesterCreditVO(1, "r1", "2018-06-12", 200);
    private RequesterCreditVO requesterCreditVO2 = new RequesterCreditVO(2, "r2", "2017-06-12", 0);



    @Test
    public void toPOJO() {
        WorkerCredit expected1 = workerCredit1;
        WorkerCredit expected2 = workerCredit2;

        WorkerCredit actual1 = creditConverter.toPOJO(workerCreditVO1, 1, 1);
        WorkerCredit actual2 = creditConverter.toPOJO(workerCreditVO2, 2, 3);

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    public void toW_CreditList() {
        List<WorkerCreditVO> expected = new ArrayList<>();
        expected.add(workerCreditVO1);
        expected.add(workerCreditVO2);

        List<WorkerCredit> list = new ArrayList<>();
        list.add(workerCredit1);
        list.add(workerCredit2);

        List<WorkerCreditVO> actual = creditConverter.toW_CreditList(list);

        assertEquals(expected, actual);
    }

    @Test
    public void toR_CreditList() {
        List<RequesterCreditVO> expected = new ArrayList<>();
        expected.add(requesterCreditVO1);
        expected.add(requesterCreditVO2);

        List<RequesterCredit> list = new ArrayList<>();
        list.add(requesterCredit1);
        list.add(requesterCredit2);

        List<RequesterCreditVO> actual = creditConverter.toR_CreditList(list);

        assertEquals(expected, actual);
    }
}