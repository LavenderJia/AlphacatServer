package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @see com.alphacat.pojo.EndedTask
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EndedTaskVO {

    private int id;
    private String name;
    private String startTime, endTime;
    private int workerCount;
    private double tagRate;
    private int costCredit;

}
