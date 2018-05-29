package com.alphacat.tag.square;

import com.alphacat.pojo.SquareTag;
import com.alphacat.vo.SquareVO;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SquareTagConverter {

    @Autowired
    private Mapper mapper;

    public SquareVO toVO(SquareTag squareTag) {
        SquareVO result = mapper.map(squareTag, SquareVO.class);
        String[] labels = squareTag.getLabelData().split(",");
        Map<String, String> labelData = new HashMap<>();
        for(String label: labels) {
            int index = label.indexOf(':');
            if(index <= 0 || index + 1 >= label.length()) {
                throw new StringIndexOutOfBoundsException("Label is in the wrong form: " + label);
            }
            labelData.put(label.substring(0, index), label.substring(index + 1));
        }
        result.setLabelData(labelData);
        return result;
    }

    public List<SquareVO> toVOList(List<SquareTag> squareTags) {
        if(squareTags == null) {
            return new ArrayList<>();
        }
        return squareTags.stream().map(this::toVO)
                .collect(Collectors.toList());
    }

    public SquareTag toPOJO(SquareVO squareVO, int taskId, int workerId, int picIndex) {
        SquareTag result = mapper.map(squareVO, SquareTag.class);
        result.setTaskId(taskId);
        result.setWorkerId(workerId);
        result.setPicIndex(picIndex);
        Map<String, String> labels = squareVO.getLabelData();
        if(labels.isEmpty()) {
            throw new RuntimeException("There is no label data in this squareVO.");
        }
        StringBuffer labelData = new StringBuffer();
        for(Map.Entry<String, String> e: labels.entrySet()) {
            labelData.append(e.getKey() + ':' + e.getValue() + ',');
        }
        result.setLabelData(labelData.substring(0, labelData.length() - 1));
        return result;
    }

}
