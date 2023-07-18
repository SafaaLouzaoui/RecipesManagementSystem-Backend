package com.gestion.recettes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)

public class CookingRecipesManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CookingRecipesManagementSystemApplication.class, args);
	}

}
