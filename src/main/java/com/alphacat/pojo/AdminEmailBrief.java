package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * This is meant for admin email list view.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminEmailBrief {

    private int id;
    private String title;
    private Timestamp time;
    private int scope; // 0 for all, 1 for requester, 2 for worker

}
