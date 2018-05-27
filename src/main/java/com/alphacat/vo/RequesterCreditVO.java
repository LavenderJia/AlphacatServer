package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequesterCreditVO {

    private int id;
    private String name;
    private String date;
    private int sum;

}
