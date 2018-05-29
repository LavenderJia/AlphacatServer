package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * A record of credit transactions of a requester.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequesterCredit {

    private int id;
    private String name;
    private Date date;
    private int sum;

}
