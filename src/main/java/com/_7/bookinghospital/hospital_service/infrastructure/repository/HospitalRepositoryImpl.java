package com._7.bookinghospital.hospital_service.infrastructure.repository;

import com._7.bookinghospital.hospital_service.domain.model.Hospital;
import com._7.bookinghospital.hospital_service.domain.repository.HospitalRepository;
import com._7.bookinghospital.hospital_service.infrastructure.repository.jpa.HospitalJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HospitalRepositoryImpl implements HospitalRepository {
    private final HospitalJpaRepository jpaRepository;

    @Override
    public Hospital save(Hospital hospital) {
        return jpaRepository.save(hospital);
    }
}
