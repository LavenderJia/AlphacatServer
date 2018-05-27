package com.alphacat.tag.square;

import com.alphacat.mapper.*;
import com.alphacat.pojo.SquareTag;
import com.alphacat.pojo.TaskRecord;
import com.alphacat.service.SquareService;
import com.alphacat.tag.CreditTransactor;
import com.alphacat.vo.SquareVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SquareServiceImpl implements SquareService {

    @Autowired
    private SquareTagMapper squareTagMapper;
    @Autowired
    private TaskRecordMapper taskRecordMapper;
    @Autowired
    private SquareTagConverter squareTagConverter;
    @Autowired
    private CreditTransactor creditTransactor;

    @Override
    public List<SquareVO> getSquares(int workerId, int taskId, int picIndex) {
        List<SquareTag> squareTags = squareTagMapper.get(workerId, taskId, picIndex);
        return squareTagConverter.toVOList(squareTags);
    }

    @Override
    public void saveSquares(List<SquareVO> squares, int workerId, int taskId, int picIndex) {
        boolean exist = squareTagMapper.isExist(workerId, taskId, picIndex);
        // case 1: 'squares' has data to store
        if(squares != null && !squares.isEmpty()) {
            // case 1.1: the first startDate to tag this picture: add record and credit
            if(!exist) {
                taskRecordMapper.incPicDoneNum(workerId, taskId);
                creditTransactor.picTransact(taskId, workerId, false);
                TaskRecord record = taskRecordMapper.get(workerId, taskId);
                if(record.getPicDoneNum() == record.getPicOrder().length() / 2) {
                    // the last picture: gained creditFinished
                    creditTransactor.taskTransact(taskId, workerId, false);
                }
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
        if((squares == null || squares.isEmpty()) && exist) {
            deleteSquares(workerId, taskId, picIndex);
            return;
        }
    }

    @Override
    public void deleteSquares(int workerId, int taskId, int picIndex) {
        boolean exist = squareTagMapper.isExist(workerId, taskId, picIndex);
        if(!exist) {
            return;
        }
        squareTagMapper.delete(workerId, taskId, picIndex);
        TaskRecord record = taskRecordMapper.get(workerId, taskId);
        if(record.getPicDoneNum() == record.getPicOrder().length() / 2) {
            // lost the creditFinished
            creditTransactor.taskTransact(taskId, workerId, true);
        }
        taskRecordMapper.decPicDoneNum(workerId, taskId);
        creditTransactor.picTransact(taskId, workerId, true);
    }

}
