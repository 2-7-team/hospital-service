package com._7.bookinghospital.hospital_service.domain.repository;
import com._7.bookinghospital.hospital_service.domain.model.Hospital;

import java.util.List;
import java.util.UUID;

public interface HospitalRepository {
    Hospital save(Hospital hospital);
    Hospital findOneHospital(UUID id);
    List<Hospital> findAllHospitals();
    boolean existsHospital(UUID id);
}
