package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequesterTaskVO {

    private int id;
    private String name;
    private double state;
    private int workerCount;

}
