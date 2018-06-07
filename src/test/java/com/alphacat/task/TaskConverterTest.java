package com.alphacat.task;

import com.alphacat.pojo.Label;
import com.alphacat.pojo.Requester;
import com.alphacat.pojo.RequesterTask;
import com.alphacat.pojo.Task;
import com.alphacat.vo.LabelVO;
import com.alphacat.vo.RequesterTaskVO;
import com.alphacat.vo.RequesterVO;
import com.alphacat.vo.TaskVO;
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
public class TaskConverterTest {
    @Autowired
    private TaskConverter taskConverter;

    private Date start1 = Date.valueOf("2018-03-29");
    private Date end1 = Date.valueOf("2018-06-29");
    private Date start2 = Date.valueOf("2018-06-06");
    private Date end2 = Date.valueOf("2018-06-10");


    private Task task1 = new Task(1, 1, "task1", "this is task1", 10 ,50, 1, false, start1, end1);
    private Task task2 = new Task(2, 2, "task2", null, 15, 20, 2, true, start2, end2);

    private LabelVO labelVO11 = new LabelVO("性别", new ArrayList<String>(){{add("女"); add("男");}});
    private LabelVO labelVO12 = new LabelVO("age", new ArrayList<String>(){{add("10"); add("20");}});
    private LabelVO labelVO21 = new LabelVO("飞机", new ArrayList<String>(){{add("");}});
    private LabelVO labelVO22 = new LabelVO("hobby", new ArrayList<String>(){{add("game"); add("sport");}});

    private Label label11 = new Label(1, "性别", new ArrayList<String>(){{add("女"); add("男");}});
    private Label label21 = new Label(2, "飞机", new ArrayList<String>(){{add("");}});


    private RequesterTask requesterTask1 = new RequesterTask(1, "requesterTask1", 1, 10);
    private RequesterTask requesterTask2 = new RequesterTask(2, "requesterTask2", 2, 20);



    @Test
    public void toPOJO() {
        Task expected1 = task1;
        Task expected2 = task2;

        List<LabelVO> labels1 = new ArrayList<>();
        List<LabelVO> labels2 = new ArrayList<>();
        labels1.add(labelVO11);
        labels1.add(labelVO12);
        labels2.add(labelVO21);
        labels2.add(labelVO22);

        TaskVO taskVO1 = new TaskVO(1, 1, "task1", "this is task1", 10, 50, 1, false,"2018-03-29", "2018-06-29", labels1);
        TaskVO taskVO2 = new TaskVO(2, 2, "task2", null, 15, 20, 2, true,"2018-06-06", "2018-06-10", labels2);

        Task actual1 = taskConverter.toPOJO(taskVO1);
        Task actual2 = taskConverter.toPOJO(taskVO2);

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    public void toVO() {
        List<LabelVO> labels1 = new ArrayList<>();
        List<LabelVO> labels2 = new ArrayList<>();
        labels1.add(labelVO11);
        labels2.add(labelVO21);

        TaskVO expected1 = new TaskVO(1, 1, "task1", "this is task1", 10, 50, 1, false,"2018-03-29", "2018-06-29", labels1);
        TaskVO expected2 = new TaskVO(2, 2, "task2", null, 15, 20, 2, true,"2018-06-06", "2018-06-10", labels2);

        TaskVO actual1 = taskConverter.toVO(task1);
        TaskVO actual2 = taskConverter.toVO(task2);

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    public void toRequesterTaskVO() {
        RequesterTaskVO expected1 = new RequesterTaskVO(1, "requesterTask1", 1, 10);
        RequesterTaskVO expected2 = new RequesterTaskVO(2, "requesterTask2", 2, 20);

        RequesterTaskVO actual1 = taskConverter.toVO(requesterTask1);
        RequesterTaskVO actual2 = taskConverter.toVO(requesterTask2);

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    public void toRequesterVOList() {
        List<RequesterTaskVO> expected = new ArrayList<>();
        RequesterTaskVO expected1 = new RequesterTaskVO(1, "requesterTask1", 1, 10);
        RequesterTaskVO expected2 = new RequesterTaskVO(2, "requesterTask2", 2, 20);
        expected.add(expected1);
        expected.add(expected2);

        List<RequesterTask> list = new ArrayList<>();
        list.add(requesterTask1);
        list.add(requesterTask2);

        List<RequesterTaskVO> actual = taskConverter.toRequesterVOList(list);

        assertEquals(expected, actual);
    }

    @Test
    public void toLabelPOJO() {
        Label expected1 = label11;
        Label expected2 = label21;

        Label actual1 = taskConverter.toPOJO(labelVO11, 1);
        Label actual2 = taskConverter.toPOJO(labelVO21, 2);

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    public void toLabelVO() {
        LabelVO expected1 = labelVO11;
        LabelVO expected2 = labelVO21;

        LabelVO actual1 = taskConverter.toVO(label11);
        LabelVO actual2 = taskConverter.toVO(label21);

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    public void toLabelVOList() {
        List<LabelVO> expected = new ArrayList<>();
        expected.add(labelVO11);
        expected.add(labelVO21);

        List<Label> list = new ArrayList<>();
        list.add(label11);
        list.add(label21);

        List<LabelVO> actual = taskConverter.toLabelVOList(list);

        assertEquals(expected, actual);
    }

    @Test
    public void toAvailableVO() {
    }

    @Test
    public void toAvailableVOList() {
    }

    @Test
    public void toHistoryVO() {
    }

    @Test
    public void toHistoryVOList() {
    }

    @Test
    public void toBriefVOList() {
    }
}