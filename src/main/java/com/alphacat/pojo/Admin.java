package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The administrator of this system. 
 * @author 161250192
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {

	private int id;
	private String name;
	private String actualName;
	private int sex; // 0 male, 1 female
	private int auth; // TODO

}
