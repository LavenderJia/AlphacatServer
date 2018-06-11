package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNoticeBriefVO {

    private int id;
    private String title;
    private String content;
    /**
     * The date when this notice has been published.
     */
    private String date;
    /**
     * 0 for not read, 1 for already read
     */
    private int state;

}
