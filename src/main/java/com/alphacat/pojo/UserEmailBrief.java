package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * This is meant for the email list of user's view.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEmailBrief {

    private int id;
    private String title;
    private Timestamp time;
    private int state; // 0 for not read, 1 for already read

}
