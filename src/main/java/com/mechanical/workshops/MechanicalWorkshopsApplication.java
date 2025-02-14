package com.mechanical.workshops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MechanicalWorkshopsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MechanicalWorkshopsApplication.class, args);
	}

}
