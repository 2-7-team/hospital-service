package com._7.bookinghospital.hospital_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients // Spring 이 FeignClient 를 자동으로 스캔, 빈으로 등록하도록 설정
public class HospitalServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(HospitalServiceApplication.class, args);
	}
}
