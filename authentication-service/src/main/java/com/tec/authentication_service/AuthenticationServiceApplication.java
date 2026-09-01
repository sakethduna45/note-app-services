package com.tec.authentication_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthenticationServiceApplication {

	public static void main(String[] args) {
        System.out.println("DB_URL = " + System.getenv("DB_URL"));
		SpringApplication.run(AuthenticationServiceApplication.class, args);
	}

}
