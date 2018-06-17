package com.alphacat.tag.square;

import com.alphacat.mapper.*;
import com.alphacat.pojo.SquareTag;
import com.alphacat.pojo.Task;
import com.alphacat.pojo.TaskRecord;
import com.alphacat.service.SquareService;
import com.alphacat.vo.SquareVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class SquareServiceImpl implements SquareService {

    @Autowired
    private SquareTagMapper squareTagMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskRecordMapper taskRecordMapper;
    @Autowired
    private SquareTagConverter squareTagConverter;

    @Override
    public List<SquareVO> getSquares(int workerId, int taskId, int picIndex) {
        List<SquareTag> squareTags = squareTagMapper.get(workerId, taskId, picIndex);
        return squareTagConverter.toVOList(squareTags);
    }

    @Override
    public void saveSquares(List<SquareVO> squares, int workerId, int taskId, int picIndex) {
        // case 0: if task ended, no action
        if(taskEnded(taskId)) {
            System.out.println("Task " + taskId + " has ended. Tags will not be saved.");
            return;
        }
        boolean exist = squareTagMapper.isExist(workerId, taskId, picIndex);
        // case 1: 'squares' has data to store
        if(squares != null && !squares.isEmpty()) {
            // case 1.1: the first time to tag this picture: add record and credit
            if(!exist) {
                taskRecordMapper.incPicDoneNum(workerId, taskId);
            }
            // Delete original data first, and then store new data.
            // Even if there's no data to delete, it will not cause error.
            squareTagMapper.delete(workerId, taskId, picIndex);
            for (SquareVO squareVO : squares) {
                squareTagMapper.add(squareTagConverter
                        .toPOJO(squareVO, taskId, workerId, picIndex));
            }
            return;
        }
        // case 2: no data means to delete
        if(exist) {
            deleteSquares(workerId, taskId, picIndex);
        }
    }

    @Override
    public void deleteSquares(int workerId, int taskId, int picIndex) {
        boolean exist = squareTagMapper.isExist(workerId, taskId, picIndex);
        if(!exist) {
            return;
        }
        squareTagMapper.delete(workerId, taskId, picIndex);
        taskRecordMapper.decPicDoneNum(workerId, taskId);
    }

    private boolean taskEnded(int taskId) {
        Task task = taskMapper.get(taskId);
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(task.getEndTime());
        endTime.add(Calendar.DAY_OF_MONTH, 1);
        return endTime.getTime().before(Calendar.getInstance().getTime());
    }

}
