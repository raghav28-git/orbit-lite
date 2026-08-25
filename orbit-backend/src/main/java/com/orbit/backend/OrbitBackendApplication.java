package com.orbit.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class OrbitBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrbitBackendApplication.class, args);
	}

}
