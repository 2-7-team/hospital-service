package com._7.bookinghospital.hospital_service.infrastructure.repository.jpa;

import com._7.bookinghospital.hospital_service.domain.model.PatientCapacityPerHour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

// PatientCapacityPerHourJpaRepository 인터페이스가 JpaRepository 인터페이스를 상속함으로써
// PatientCapacityPerHourJpaRepository 인터페이스의 구현체는 SimpleJpaRepository 클래스가 된다.
public interface PatientCapacityPerHourJpaRepository extends JpaRepository<PatientCapacityPerHour, UUID> {
}
