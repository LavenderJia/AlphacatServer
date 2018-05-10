package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
/**
 * 工人VO，没有经验值与积分
 * @author 161250102
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerVO {

    private int id;
    private String name;
    private String birth;
    private int sex; // 0 is male; 1 is female
    private String email;
    private String signature;
    private int exp;
    private int credit;
    private int state;

}
