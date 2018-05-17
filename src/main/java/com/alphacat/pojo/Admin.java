package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The administrator of this system. 
 * @author 161250192
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Admin {

	private int id;
	private String name;
	private String actualName;
	private int sex; // 0 male, 1 female
	private int auth; // 0 superAdmin, 1 requesterAdmin, 2 workerAdmin,

}
