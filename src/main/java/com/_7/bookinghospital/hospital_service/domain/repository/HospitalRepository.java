package com._7.bookinghospital.hospital_service.domain.repository;

import com._7.bookinghospital.hospital_service.domain.model.Hospital;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HospitalRepository {
    Hospital save(Hospital hospital);
    Optional<Hospital> findByHospitalId(UUID id);
    Page<Hospital> findAllHospitals(Pageable pageable);

    // 내부용
    Optional<List<Hospital>> findAll();

    // (의문) 이미 유효성 검사를 했지만 여기서도 한 번 더 해야할 것인가?
    boolean existsByPhone(@NotBlank(message = "병원 전화번호는 필수입니다.") String phone);
}
