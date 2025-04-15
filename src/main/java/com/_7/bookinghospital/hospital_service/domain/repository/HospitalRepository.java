package com._7.bookinghospital.hospital_service.domain.repository;

import com._7.bookinghospital.hospital_service.domain.model.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface HospitalRepository {
    Hospital save(Hospital hospital);
    Optional<Hospital> findByHospitalId(UUID id);
    Page<Hospital> findAllHospitals(Pageable pageable);
}
