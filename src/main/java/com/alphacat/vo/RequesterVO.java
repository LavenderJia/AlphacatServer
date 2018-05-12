package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 发起者VO
 * @author 161250102
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class RequesterVO {

    private int id;
    private String name;
    private String birth;
    private int sex; // 0 is male; 1 is female
    private String email;
    private String occupation;
    private String company;
    private int state;

}
