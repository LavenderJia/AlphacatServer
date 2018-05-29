package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * This is meant for the notice list of user's view.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserNoticeBrief {

    private int id;
    private String title;
    /**
     * The date when this notice has been published.
     */
    private Timestamp date;
    /**
     * 0 for not read, 1 for already read
     */
    private int state;

}
