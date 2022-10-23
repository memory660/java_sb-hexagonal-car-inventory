package com.alvesjv.projecthexagonalfull;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ProjectHexagonalFullApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectHexagonalFullApplication.class, args);
	}

}
