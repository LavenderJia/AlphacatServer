package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
/**
 * A notice published by an administrator.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notice {

    private int id;
    private String title;
    private String content;
    /**
     * The date when this notice has been published.
     */
    private Timestamp startDate;
    /**
     * Null if this notice has no end date.
     */
    private Timestamp endDate;
    /**
     * 0 for all, 1 for requester, 2 for worker
     */
    private int type;

}
