package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
/**
 * 邮件/公告
 * @author 161250102
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    private int id;
    private Timestamp time;
    private int scope;
    private String title;
    private String content;

}
