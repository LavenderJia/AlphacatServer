package com.alphacat.task;

import com.alphacat.pojo.*;
import com.alphacat.vo.*;
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

    private AvailableTask a1 = new AvailableTask(1, "a1", 10, 50, 1, Date.valueOf("2018-06-29"));
    private AvailableTask a2 = new AvailableTask(2, "a2", 0, 100, 2, Date.valueOf("2018-04-29"));

    private HistoryTask h1 = new HistoryTask(1, "h1", Date.valueOf("2018-12-31"), 250, 0.9234, 0.3);
    private HistoryTask h2 = new HistoryTask(2, "h2", Date.valueOf("2017-12-31"), 0, 0.0000, 0.4);



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
        AvailableTaskVO expected1 = new AvailableTaskVO(1, "a1", 10, 50, 1, "2018-06-29");
        AvailableTaskVO expected2 = new AvailableTaskVO(2, "a2", 0, 100, 2, "2018-04-29");

        AvailableTaskVO actual1 = taskConverter.toAvailableVO(a1);
        AvailableTaskVO actual2 = taskConverter.toAvailableVO(a2);

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);

    }

    @Test
    public void toAvailableVOList() {
        AvailableTaskVO expected1 = new AvailableTaskVO(1, "a1", 10, 50, 1, "2018-06-29");
        AvailableTaskVO expected2 = new AvailableTaskVO(2, "a2", 0, 100, 2, "2018-04-29");

        List<AvailableTaskVO> expected = new ArrayList<>();
        expected.add(expected1);
        expected.add(expected2);

        List<AvailableTask> list = new ArrayList<>();
        list.add(a1);
        list.add(a2);

        List<AvailableTaskVO> actual = taskConverter.toAvailableVOList(list);

        assertEquals(expected,actual);
    }

    @Test
    public void toHistoryVO() {
        HistoryTaskVO expected1 = new HistoryTaskVO(1, "h1",  "2018-12-31", 250, 0.9234, 0.3);
        HistoryTaskVO expected2 = new HistoryTaskVO(2, "h2", "2017-12-31", 0, 0.0000, 0.400);

        HistoryTaskVO actual1 = taskConverter.toHistoryVO(h1);
        HistoryTaskVO actual2 = taskConverter.toHistoryVO(h2);

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    public void toHistoryVOList() {
        HistoryTaskVO expected1 = new HistoryTaskVO(1, "h1",  "2018-12-31", 250, 0.9234, 0.3);
        HistoryTaskVO expected2 = new HistoryTaskVO(2, "h2", "2017-12-31", 0, 0.0000, 0.4000);

        List<HistoryTaskVO> expected = new ArrayList<>();
        expected.add(expected1);
        expected.add(expected2);

        List<HistoryTask> list = new ArrayList<>();
        list.add(h1);
        list.add(h2);

        List<HistoryTaskVO> actual = taskConverter.toHistoryVOList(list);

        assertEquals(expected, actual);
    }

    @Test
    public void toBriefVOList() {
        TaskBriefVO expected1 = new TaskBriefVO(1,"t1");
        TaskBriefVO expected2 = new TaskBriefVO(2,"t2");
        TaskBriefVO expected3 = new TaskBriefVO(0,"t1");

        List<TaskBriefVO> expected = new ArrayList<>();
        expected.add(expected1);
        expected.add(expected2);
        expected.add(expected3);

        TaskBrief t1 = new TaskBrief(1, "t1");
        TaskBrief t2 = new TaskBrief(2, "t2");
        TaskBrief t3 = new TaskBrief(0, "t1");

        List<TaskBrief> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);
        list.add(t3);

        List<TaskBriefVO> actual = taskConverter.toBriefVOList(list);

        assertEquals(expected,actual);
    }
}