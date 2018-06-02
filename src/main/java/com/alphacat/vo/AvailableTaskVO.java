package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @see com.alphacat.pojo.AvailableTask
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AvailableTaskVO {

    private int id;
    private String name;
    private int creditPerPic;
    private int creditFinished;
    private int method;
    private String endTime;

}
