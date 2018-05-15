package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @see com.alphacat.pojo.IdleTask
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class IdleTaskVO {

    private int id;
    private String name;
    private int creditPerPic;
    private int creditFinished;
    private int method; // 1 - single, 2 - multiple, 3 - irregular
    private String startTime;
    private String endTime;

}
