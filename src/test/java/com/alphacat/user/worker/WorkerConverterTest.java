package com.alphacat.user.worker;

import com.alphacat.pojo.Worker;
import com.alphacat.vo.WorkerVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WorkerConverterTest {

    @Autowired
    private WorkerConverter workerConverter;

    @Test
    public void toWorkerVO() {
        Date date = Date.valueOf("1995-11-18");
        WorkerVO expected = new WorkerVO(1, "w1", "1995-11-18", 1, "email1", "aaa", 0,0, 0);
        WorkerVO result = workerConverter.toVO(new Worker(1, "w1",date,1,"email1","aaa",0,0,0));
        assertEquals(expected, result);
    }

    @Test
    public void toWorkerPOJO() {
        Date date = Date.valueOf("1995-11-18");
        Worker expected = new Worker(1, "w1", date, 1, "email1", "aaa", 0,0, 0);
        Worker result = workerConverter.toPOJO(new WorkerVO(1, "w1","1995-11-18",1,"email1","aaa",0,0,0));
        assertEquals(expected, result);
    }


}
