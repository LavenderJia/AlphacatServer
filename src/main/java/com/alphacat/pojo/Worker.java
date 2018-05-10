package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * 工人
 * @author 161250102
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Worker {
   // public final static int ACTIVE = 0;
    //public final static int FORBIDDEN = 1;

    private int id;
    private String name;
    private Date birth;
    private int sex; // 0 is male; 1 is female
    private String email;
    private String signature;
    private int exp;
    private int credit;
    private int state;

}
