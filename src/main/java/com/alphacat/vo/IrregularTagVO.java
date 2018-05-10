package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 不规则标注
 * @author 161250102
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IrregularTagVO {

    private int picIndex;
    private int taskId;
    private List<List<PointVO>> paths;

}
