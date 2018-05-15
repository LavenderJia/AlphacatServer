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

    private Mapper mapper = DozerBeanMapperBuilder.create()
            .withMappingFiles("config/dozer-mapping.xml").build();

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
        return tasks.stream().map(this::toVO)
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
        return tasks.stream().map(this::toUnderwayVO)
                .collect(Collectors.toList());
    }

    public EndedTaskVO toEndedVO(EndedTask task) {
        return mapper.map(task, EndedTaskVO.class);
    }

    public List<EndedTaskVO> toEndedVOList(List<EndedTask> tasks) {
        return tasks.stream().map(this::toEndedVO)
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
        return labels.stream().map(this::toVO)
                .collect(Collectors.toList());
    }

    public AvailableTaskVO toAvailableVO(AvailableTask task) {
        AvailableTaskVO result = mapper.map(task, AvailableTaskVO.class);
        if(task.getProgress() == null) {
            result.setState(1);
        } else if(task.getProgress() == 1.0D) {
            result.setState(3);
        } else {
            result.setState(2);
        }
        return result;
    }

    public List<AvailableTaskVO> toAvailableVOList(List<AvailableTask> tasks) {
        return tasks.stream().map(this::toAvailableVO)
                .collect(Collectors.toList());
    }

    public HistoryTaskVO toHistoryVO(HistoryTask historyTask) {
        return mapper.map(historyTask, HistoryTaskVO.class);
    }

    public List<HistoryTaskVO> toHistoryVOList(List<HistoryTask> historyTasks) {
        return historyTasks.stream().map(this::toHistoryVO)
                .collect(Collectors.toList());
    }

}