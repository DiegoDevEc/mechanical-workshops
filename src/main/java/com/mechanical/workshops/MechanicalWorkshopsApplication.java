package com.mechanical.workshops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling  // Habilita tareas programadas
public class MechanicalWorkshopsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MechanicalWorkshopsApplication.class, args);
	}

}
