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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private PictureMapper pictureMapper;

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

    @Override
    public boolean testAnswer(int taskId, int workerId) {
        if(taskId < 1 || taskId > 3) {
            // it is not one of test tasks
            return false;
        }
        TaskRecord record = taskRecordMapper.get(workerId, taskId);
        if(record.getPicDoneNum() < record.getPicOrder().length() / 2) {
            // the worker has not finished the task
            return false;
        }
        boolean passed = false;
        switch (taskId) {
            case 1: {
                passed = pictureMapper.get(taskId).stream()
                        .allMatch(p -> this.testSingleSquare(taskId, workerId, p, 0));
                break;
            }
            case 2: {
                passed = pictureMapper.get(taskId).stream().allMatch(p -> squareTagMapper.get(0, taskId, p).stream()
                    .mapToInt(SquareTag::getSquareIndex).allMatch(s -> this.testSingleSquare(taskId, workerId, p, s)));
                break;
            }
            case 3: {
                // cannot test at present...
                break;
            }
        }
        if(passed) {
            taskRecordMapper.setAccuracy(taskId, workerId, 1.0, 1.0);
        }
        return passed;
    }

    @Override
    public List<SquareVO> getGoldAnswer(int taskId, int picIndex) {
        return squareTagConverter.toVOList(squareTagMapper.get(0, taskId, picIndex));
    }

    private boolean testSingleSquare(int taskId, int workerId, int picIndex, int squareIndex) {
        final int d = 10;
        SquareVO answer = squareTagConverter.toVO(squareTagMapper.get(workerId, taskId, picIndex).get(squareIndex));
        SquareVO gold = squareTagConverter.toVO(squareTagMapper.get(0, taskId, picIndex).get(squareIndex));
        Map<String, String> workerLabels = answer.getLabelData();
        Map<String, String> goldLabels = gold.getLabelData();
        return Math.abs(answer.getX() - gold.getX()) <= d && Math.abs(answer.getY() - gold.getY()) <= d
                && Math.abs(answer.getW() - gold.getW()) <= d && Math.abs(answer.getH() - gold.getH()) <= d
                && goldLabels.entrySet().stream().allMatch(e -> e.getValue().equals(workerLabels.get(e.getKey())));
    }

    private boolean taskEnded(int taskId) {
        Task task = taskMapper.get(taskId);
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(task.getEndTime());
        endTime.add(Calendar.DAY_OF_MONTH, 1);
        return endTime.getTime().before(Calendar.getInstance().getTime());
    }

}
