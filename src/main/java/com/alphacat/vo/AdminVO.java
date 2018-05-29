package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AdminVO {

	private int id;
	private String name;
	private String actualName;
	private int sex; // 0 male, 1 female
    /**
     * 0 for super admin, 1 for requester admin, 2 for worker admin
     */
	private int auth;

}
