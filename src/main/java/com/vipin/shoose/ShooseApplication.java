package com.vipin.shoose;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShooseApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShooseApplication.class, args);
	}

}
