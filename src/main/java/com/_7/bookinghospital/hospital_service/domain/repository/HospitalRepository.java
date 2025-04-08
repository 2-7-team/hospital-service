package com._7.bookinghospital.hospital_service.domain.repository;

import com._7.bookinghospital.hospital_service.domain.model.Hospital;

public interface HospitalRepository {
    Hospital save(Hospital hospital);
}
