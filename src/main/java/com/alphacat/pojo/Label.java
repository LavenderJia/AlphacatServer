package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * 任务标签
 * @author 161250102
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Label {

    private int taskId;
    private String label;
    private ArrayList<String> choices;

}
