package com._7.bookinghospital.hospital_service.infrastructure.repository.jpa;

import com._7.bookinghospital.hospital_service.domain.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

// ScheduleJpaRepository 인터페이스가 JpaRepository 인터페이스를 상속함으로써
// ScheduleJpaRepository 인터페이스의 구현체는 SimpleJpaRepository 객체가 된다.
public interface ScheduleJpaRepository extends JpaRepository<Schedule, UUID> {
}
