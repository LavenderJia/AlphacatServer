package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDetailVO {

    private int id;
    private String title;
    private String content;
    /**
     * its start date
     */
    private String date;

}
