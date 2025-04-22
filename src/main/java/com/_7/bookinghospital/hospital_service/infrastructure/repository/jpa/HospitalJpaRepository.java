package com._7.bookinghospital.hospital_service.infrastructure.repository.jpa;

import com._7.bookinghospital.hospital_service.domain.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

// DB 와 연결, JPA
public interface HospitalJpaRepository extends JpaRepository<Hospital, UUID> {
    boolean existsByPhone(String phone);

    @Query("SELECT h FROM Hospital h WHERE h.id =:id AND h.isDeleted = false")
    Optional<Hospital> isActiveHospital(@Param("id") UUID id);
}