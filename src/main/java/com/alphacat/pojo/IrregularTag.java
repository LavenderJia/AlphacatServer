package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 不规则边框标注
 * @author 161250102
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class IrregularTag {

    private int workerId;
    private int taskId;
    private int picIndex;
    private String figure; //不规则边界信息

}
