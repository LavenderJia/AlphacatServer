package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @see com.alphacat.pojo.UnderwayTask
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UnderwayTaskVO {

    private int id;
    private String name;
    private String startTime;
    private String endTime;
    private int workerCount;
    private double tagRate;

}
