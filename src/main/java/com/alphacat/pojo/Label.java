package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * 任务标签
 * @author 161250102
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Label {

    private int taskId;
    private String label;
    private ArrayList<String> choices;

}
