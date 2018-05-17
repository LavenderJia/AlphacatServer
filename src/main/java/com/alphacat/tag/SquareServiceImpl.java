package com.alphacat.service.impl;

import com.alphacat.mapper.SquareTagMapper;
import com.alphacat.mapper.TaskRecordMapper;
import com.alphacat.service.SquareService;
import com.alphacat.util.BeanMapper;
import com.alphacat.vo.SquareVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SquareServiceImpl implements SquareService {

    @Autowired
    private SquareTagMapper squareTagMapper;
    @Autowired
    private TaskRecordMapper taskRecordMapper;

    @Override
    public List<SquareVO> getSquares(int workerId, int taskId, int picIndex) {
        return squareTagMapper.getTagsByWorker(workerId, taskId, picIndex)
                .stream()
                .map(BeanMapper::toSquareVO)
                .collect(Collectors.toList());
    }

    @Override
    public void saveSqaures(List<SquareVO> squares, int workerId, int taskId, int picIndex) {
        if (! squareTagMapper.isExistTag(workerId, taskId, picIndex)) taskRecordMapper.incPicDoneNum(workerId, taskId);
        squareTagMapper.deleteTagsByWorker(workerId, taskId, picIndex);
        if (squares == null || squares.size() == 0) {
            taskRecordMapper.decPicDoneNum(workerId, taskId);
            return;
        }
        for (SquareVO squareVO : squares) {
            squareTagMapper.addTags(BeanMapper.toSquareTag(squareVO, workerId, taskId, picIndex));
        }

    }
}
