package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 不规则标注
 * @author 161250102
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class IrregularTagVO {

    private List<List<PointVO>> paths;

}
