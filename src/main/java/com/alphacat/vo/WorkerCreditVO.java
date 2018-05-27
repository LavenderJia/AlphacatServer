package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @see com.alphacat.pojo.WorkerCredit
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerCreditVO {

    private String date;
    private String info;
    private int change;
    private int credit;

}
