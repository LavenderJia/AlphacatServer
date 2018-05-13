package com.alphacat.task;

import com.alphacat.mapper.LabelMapper;
import com.alphacat.pojo.HistoryTask;
import com.alphacat.pojo.Label;
import com.alphacat.pojo.Task;
import com.alphacat.vo.HistoryTaskVO;
import com.alphacat.vo.LabelVO;
import com.alphacat.vo.R_TaskVO;
import com.alphacat.vo.TaskVO;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskConverter {

    @Autowired
    private LabelMapper labelMapper;

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

    public R_TaskVO toRVO(Task task) {
        R_TaskVO result = mapper.map(task, R_TaskVO.class);
        // set up its state
        Date now = new Date();
        int comparedNum = now.compareTo(task.getStartTime()) + now.compareTo(task.getEndTime());
        if(comparedNum == -2) { // -1 + -1: task hasn't started
            result.setState(0);
            result.setWorkerCount(0);
            result.setTagRate(0);
            return result;
        }
        if(comparedNum == 2) { // 1 + 1: task has ended
            result.setState(2);
        } else {
            result.setState(1);
        }
        // set up its worker count
        Integer workerCount = 0;
        // TODO
        return result;
    }

}
