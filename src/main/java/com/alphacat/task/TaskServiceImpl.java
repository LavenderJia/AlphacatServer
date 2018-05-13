package com.alphacat.task;

import com.alphacat.mapper.LabelMapper;
import com.alphacat.mapper.TaskMapper;
import com.alphacat.pojo.Task;
import com.alphacat.service.TaskService;
import com.alphacat.vo.HistoryTaskVO;
import com.alphacat.vo.R_TaskVO;
import com.alphacat.vo.TaskVO;
import com.alphacat.vo.W_TaskVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskConverter taskConverter;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private LabelMapper labelMapper;

    @Override
    public List<R_TaskVO> getR(int requesterId) {
        // TODO
        return null;
    }

    @Override
    public List<W_TaskVO> getW(int workerId) {
        // TODO
        return null;
    }

    @Override
    public TaskVO get(int id) {
        return taskConverter.toVO(taskMapper.get(id));
    }

    @Override
    public List<HistoryTaskVO> getHistoryTasks(int workerId) {
        // TODO
        return null;
    }

    @Override
    public int add(TaskVO taskVO) {
        Integer id = taskMapper.getNewId();
        if(id == null) {
            throw new NullPointerException("Cannot allocate new id for a task.");
        }
        taskVO.setId(id);
        // add task's basic info and settings
        Task task = taskConverter.toPOJO(taskVO);
        taskMapper.add(task);
        // add labels
        taskVO.getLabels().forEach(l -> labelMapper.add(taskConverter.toPOJO(l, id)));
        return id.intValue();
    }

    @Override
    public void update(TaskVO taskVO) {
        int id = taskVO.getId();
        Task task = taskConverter.toPOJO(taskVO);
        taskMapper.update(task);
        labelMapper.delete(taskVO.getId());
        taskVO.getLabels().forEach(l -> labelMapper.add(taskConverter.toPOJO(l, id)));
    }

    /**
     * Just delete the task. Mysql will delete other related data for you.
     */
    @Override
    public void delete(int id) {
        taskMapper.delete(id);
    }

}
