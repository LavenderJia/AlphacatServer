package com.alphacat.tag.irregular;

import com.alphacat.pojo.IrregularTag;
import com.alphacat.vo.IrregularTagVO;
import com.alphacat.vo.PointVO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class IrregularTagConverter {

    public IrregularTag toPOJO(IrregularTagVO tag, int taskId, int workerId, int picIndex) {
        IrregularTag result = new IrregularTag();
        result.setWorkerId(workerId);
        result.setTaskId(taskId);
        result.setPicIndex(picIndex);
        StringBuffer figureBuff = new StringBuffer();
        tag.getPaths().forEach(path -> {
            path.forEach(p -> figureBuff.append(p.getX()).append(',')
                                        .append(p.getY()).append(':'));
            if(!path.isEmpty()) {
                figureBuff.deleteCharAt(figureBuff.length() - 1);
                figureBuff.append(';');
            }
        });
        if(!tag.getPaths().isEmpty()) {
            figureBuff.deleteCharAt(figureBuff.length() - 1);
        }
        result.setFigure(figureBuff.toString());
        return result;
    }

    public IrregularTagVO toVO(IrregularTag tag) {
        IrregularTagVO result = new IrregularTagVO();
        result.setPaths(Arrays.stream(tag.getFigure().split(";"))
                            .map(this::toPath).collect(Collectors.toList()));
        return null;
    }

    public List<IrregularTagVO> toVOList(List<IrregularTag> tags) {
        if(tags == null) {
            return new ArrayList<>();
        }
        return tags.stream().map(this::toVO)
                .collect(Collectors.toList());
    }

    private List<PointVO> toPath(String pathStr) {
        if(pathStr == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(pathStr.split(":"))
                .map(s -> {
                    String[] xy = s.split(",");
                    int x = Integer.parseInt(xy[0]);
                    int y = Integer.parseInt(xy[1]);
                    return new PointVO(x, y);
                }).collect(Collectors.toList());
    }

}
