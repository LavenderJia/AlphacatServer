package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 161250102
 * 编辑邮件界面使用的邮件VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVO {

    private String title;
    private String content;
    private int type; // 0 for all, 1 for requester, 2 for worker
    private String date;

}
