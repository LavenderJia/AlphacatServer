package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Avatar {

    private String name;
    /**
     * 0 for requester, 1 for worker, 2 for admin
     */
    private int type;
    private byte[] blob;

}
