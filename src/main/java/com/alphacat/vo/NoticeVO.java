package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeVO {

    private int id;
    /**
     * the date when this notice has been published
     */
    private String startDate;
    /**
     * null if the notice has no end date
     */
    private String endDate;
    private String title;
    private String content;
    /**
     * 0 for all, 1 for requester, 2 for worker
     */
    private int type;

}
