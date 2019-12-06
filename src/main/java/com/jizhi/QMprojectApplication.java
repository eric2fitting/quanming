package com.jizhi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class QMprojectApplication {
	public static void main(String[] args) {
		SpringApplication.run(QMprojectApplication.class, args);
	}
}
