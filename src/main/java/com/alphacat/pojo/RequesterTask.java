package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The task item to be shown to requesters.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequesterTask {

    private int id;
    private String name;
    private double state;
    private int workerCount;

}
