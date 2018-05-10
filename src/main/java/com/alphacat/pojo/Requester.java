package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * 发起者
 * @author 161250102
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Requester {
    //public final static int ACTIVE = 0;
    //public final static int WAITCHECK = 1;

    private int id;
    private String name;
    private Date birth;
    private int sex; // 0 is male; 1 is female
    private String email;
    private String occupation;
    private String company;
    private int state;
}
