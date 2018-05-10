package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * 标签：数组长度为0时表明是需要工人填写的标签
 * @author 161250102
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelVO {

    private String title;
    private ArrayList<String> choices;

}
