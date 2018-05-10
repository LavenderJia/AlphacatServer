package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 161250102
 *  显示邮件界面使用的邮件VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailShowVO {

    private String title;
    private String content;
    private int state; //0-未读，1-已读
    private String time;

}
