package com._7.bookinghospital.hospital_service.infrastructure.repository.jpa;

import com._7.bookinghospital.hospital_service.domain.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalJpaRepository extends JpaRepository<Hospital, Long> {
}
