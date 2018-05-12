package com.alphacat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminVO {

	private int id;
	private String name;
	private String actualName;
	private int sex; // 0 male, 1 female
	private int auth; // TODO

}
