package com.Medicare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableScheduling
public class CapstoneMedicareProjectApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(CapstoneMedicareProjectApplication.class, args);
	}
}
