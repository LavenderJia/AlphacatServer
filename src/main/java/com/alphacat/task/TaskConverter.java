package com.alphacat.task;

import com.alphacat.mapper.LabelMapper;
import com.alphacat.mapper.PictureMapper;
import com.alphacat.mapper.TaskRecordMapper;
import com.alphacat.pojo.*;
import com.alphacat.vo.*;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskConverter {

    @Autowired
    private LabelMapper labelMapper;
    @Autowired
    private TaskRecordMapper taskRecordMapper;
    @Autowired
    private PictureMapper pictureMapper;

    private Mapper mapper = DozerBeanMapperBuilder.create()
            .withMappingFiles("config/dozer-mapping.xml").build();

    public TaskConverter() {
        System.out.println("Task Converter constructed.");
    }

    public Task toPOJO(TaskVO taskVO) {
        return mapper.map(taskVO, Task.class);
    }

    public TaskVO toVO(Task task) {
        TaskVO result = mapper.map(task, TaskVO.class);
        int id = task.getId();
        List<Label> labels = labelMapper.get(id);
        result.setLabels(toLabelVOList(labels));
        return result;
    }

    public IdleTaskVO toVO(IdleTask task) {
        return mapper.map(task, IdleTaskVO.class);
    }

    public List<IdleTaskVO> toIdleVOList(List<IdleTask> tasks) {
        return tasks.stream().map(t -> toVO(t))
                .collect(Collectors.toList());
    }

    public UnderwayTaskVO toUnderwayVO(UnderwayTask task) {
        if(task.getTagRate() == null) {
            String errMsg = "Cannot generate tagRate: the task has no picture.";
            throw new NullPointerException(errMsg);
        }
        UnderwayTaskVO result = mapper.map(task, UnderwayTaskVO.class);
        return result;
    }

    public List<UnderwayTaskVO> toUnderwayVOList(List<UnderwayTask> tasks) {
        return tasks.stream().map(t -> toUnderwayVO(t))
                .collect(Collectors.toList());
    }

    public EndedTaskVO toEndedVO(EndedTask task) {
        return mapper.map(task, EndedTaskVO.class);
    }

    public List<EndedTaskVO> toEndedVOList(List<EndedTask> tasks) {
        return tasks.stream().map(t -> toEndedVO(t))
                .collect(Collectors.toList());
    }

    public Label toPOJO(LabelVO labelVO, int taskId) {
        Label label = mapper.map(labelVO, Label.class);
        label.setTaskId(taskId);
        return label;
    }

    public LabelVO toVO(Label label) {
        return mapper.map(label, LabelVO.class);
    }

    public List<LabelVO> toLabelVOList(List<Label> labels) {
        return labels.stream().map(l -> toVO(l))
                .collect(Collectors.toList());
    }

    public HistoryTask toPOJO(HistoryTaskVO historyTaskVO) {
        return mapper.map(historyTaskVO, HistoryTask.class);
    }

    public HistoryTaskVO toVO(HistoryTask historyTask) {
        return mapper.map(historyTask, HistoryTaskVO.class);
    }

    public List<HistoryTaskVO> toHistoryTaskVOList(List<HistoryTask> historyTasks) {
        return historyTasks.stream().map(t -> toVO(t))
                .collect(Collectors.toList());
    }

    public W_TaskVO toWVO(Task task, int workerId) {
        W_TaskVO result = mapper.map(task, W_TaskVO.class);
        // set up its state
        TaskRecord record = taskRecordMapper.get(task.getId(), workerId);
        if(record == null) {
            result.setState(0);
        } else {
            Integer picNum = pictureMapper.count(task.getId());
            if(picNum == null || picNum == 0) {
                throw new RuntimeException("No picture in task: " + task.getId());
            }
            if(record.getPicDoneNum() == picNum) {
                result.setState(2);
            } else {
                result.setState(1);
            }
        }
        // set up its workerCount
        Integer workerCount = taskRecordMapper.getWorkerNum(task.getId());
        result.setWorkerCount(workerCount == null ? 0 : workerCount);
        return result;
    }

}
