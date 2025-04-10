package com._7.bookinghospital.hospital_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HospitalServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(HospitalServiceApplication.class, args);
	}
}
