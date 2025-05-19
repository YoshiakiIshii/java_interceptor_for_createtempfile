package com.example.onlinedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:applicationContext.xml")
public class OnlinedemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlinedemoApplication.class, args);
	}

}
