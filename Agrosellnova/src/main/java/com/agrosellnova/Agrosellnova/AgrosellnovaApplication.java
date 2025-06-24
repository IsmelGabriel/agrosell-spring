package com.agrosellnova.Agrosellnova;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.agrosellnova.Agrosellnova.repositorio")
@EntityScan("com.agrosellnova.Agrosellnova.modelo")
public class AgrosellnovaApplication {
	public static void main(String[] args) {
		SpringApplication.run(AgrosellnovaApplication.class, args);
	}

}
