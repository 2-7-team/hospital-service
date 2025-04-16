package com._7.bookinghospital.hospital_service.infrastructure.repository.jpa;

import com._7.bookinghospital.hospital_service.domain.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

// DB 와 연결, JPA
public interface HospitalJpaRepository extends JpaRepository<Hospital, UUID> {
    boolean existsByPhone(String phone);
}
