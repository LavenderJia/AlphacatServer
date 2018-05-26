package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminEmailBriefVO {

    private int id;
    private String title;
    private String date;
    private int type; // 0 for all, 1 for requester, 2 for worker

}
