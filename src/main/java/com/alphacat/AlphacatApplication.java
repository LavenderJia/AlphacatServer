package com.alphacat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.alphacat.mapper")
public class AlphacatApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlphacatApplication.class, args);
	}
}
