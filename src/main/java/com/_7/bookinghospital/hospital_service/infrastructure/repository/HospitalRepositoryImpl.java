package com._7.bookinghospital.hospital_service.infrastructure.repository;

import com._7.bookinghospital.hospital_service.domain.model.Hospital;
import com._7.bookinghospital.hospital_service.domain.repository.HospitalRepository;
import com._7.bookinghospital.hospital_service.infrastructure.repository.jpa.HospitalJpaRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
// 도메인>레포지토리의 인터페이스 HospitalRepository 를 인프라스트럭쳐>레포지토리 에서 구현하고,
// 인프라스트럭쳐>레포지토리>jpa 의 인터페이스(db 연결, SimpleJpaRepository 가 구현체) 를 필드로 갖는다.
public class HospitalRepositoryImpl implements HospitalRepository {
    private final HospitalJpaRepository hospitalJpaRepository;

    @Override
    public Hospital save(Hospital hospital) {
        return hospitalJpaRepository.save(hospital);
    }

    @Override
    public Hospital findOneHospital(UUID id) {
        return hospitalJpaRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("조회하신 병원 정보가 존재하지 않습니다."));
    }

    @Override
    public List<Hospital> findAllHospitals() {
        List<Hospital> hospitalList = hospitalJpaRepository.findAll();
        // 어플에 등록된 병원이 하나도 없을 때 예외 발생
        if(hospitalList.isEmpty()) throw new NotFoundException("등록된 병원이 없어 조회하실 수 없습니다.");
        return hospitalList;
    }

    @Override
    public boolean existsHospital(UUID id) {
        return hospitalJpaRepository.existsById(id);
    }
}
