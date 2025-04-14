package com._7.bookinghospital.hospital_service.application.service;

import com._7.bookinghospital.hospital_service.presentation.dto.request.CreateHospitalRequestDto;
import com._7.bookinghospital.hospital_service.presentation.dto.request.UpdateHospitalRequestDto;
import com._7.bookinghospital.hospital_service.presentation.dto.response.FindOneHospitalResponseDto;
import com._7.bookinghospital.hospital_service.presentation.dto.response.UpdateHospitalResponseDto;
import com._7.bookinghospital.hospital_service.domain.model.Hospital;
import com._7.bookinghospital.hospital_service.domain.repository.HospitalRepository;
import jakarta.validation.Valid;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HospitalService {
    private final HospitalRepository hospitalRepository;

    @Transactional
    public UUID create(@Valid CreateHospitalRequestDto dto) {
        /*
        Hospital hospital = Hospital.createHospitalBuilder()
                .name(dto.getName())
                .phone(dto.getPhone())
                .description(dto.getDescription())
                .address(dto.getAddress())
                .openHour(dto.getOpenHour())
                .closeHour(dto.getCloseHour())
                .build();
        */

        // 정적 팩토리 메서드 패턴 이용
        // (문제) 정적 팩토리 메서드 매개변수로 전달하는 값들을 더 간단히 작성할 수 있는 방법이 있는지
        //  → 여기서 계층간 dto 를 따로 작성할 필요성을 느낌
        Hospital hospital = Hospital.of(dto.getName(), dto.getPhone(), dto.getDescription(), dto.getAddress(), dto.getOpenHour(), dto.getCloseHour());

        Hospital saved = hospitalRepository.save(hospital);

        return saved.getId();
    }

    // 병원 단건 조회
    public FindOneHospitalResponseDto findOneHospital(UUID hospitalId) {
        // (예정) delete 필드의 값이 false 인 경우 조회가 되지 않도록 하기
        Hospital findHospital = hospitalRepository.findOneHospital(hospitalId);
        return new FindOneHospitalResponseDto(findHospital);
    }

    // 병원 목록 조회
    public List<FindOneHospitalResponseDto> findAllHospitals() {
        List<Hospital> hospitalList = hospitalRepository.findAllHospitals();
        return hospitalList
                .stream()
                .map(FindOneHospitalResponseDto::new) // Stream<FindOneHospitalResponseDto>
                .toList();
    }

    @Transactional
    public UpdateHospitalResponseDto updateHospitalInfo(UUID hospitalId, UpdateHospitalRequestDto updateHospitalInfo) {
        // 1. 업데이트할 병원 정보 존재하는지 고유 식별자(UUID hospitalId) 기반으로 병원 정보 확인하기
        Hospital findOneHospital = hospitalRepository.findOneHospital(hospitalId);

        // hospitalId 기반 병원 정보 존재시
        // 2. 병원 정보 수정 요청 값 유효성 검증하기
        // (의문) value 가 제네릭 타입인데 제네릭 타입에 맞게 형변환 되는 것인가?
        Map<String, Object> extractUpdateFields = updateHospitalInfo.extractUpdateFields();

        // 2-1. 업데이트 요청 데이터가 하나도 전달되지 않았다면 예외 발생 시키기
        if(extractUpdateFields.isEmpty()) {
            throw new IllegalArgumentException("수정할 데이터가 없습니다. 수정할 항목을 다시 확인해주세요.");
        }

        Set<Map.Entry<String, Object>> entries = extractUpdateFields.entrySet();

        // 3. 클라이언트가 수정 요청한 병원 정보 수정하기
        Hospital updatedHospitalInfo = findOneHospital.exchangeInfo(entries);

        // 4. 수정한 필드의 값으로 db에 저장하기
        Hospital saved = hospitalRepository.save(updatedHospitalInfo);

        return new UpdateHospitalResponseDto(saved);
    }

    @Transactional
    public void delete(UUID hospitalId) {
        // (삭제 예정) 임의 사용자 정보
        Long userId = 100L;

        if(!hospitalRepository.existsHospital(hospitalId)) {
         throw new NotFoundException("삭제하려는 병원이 존재하지 않습니다.");
        }

        Hospital deleteHospital = hospitalRepository.findOneHospital(hospitalId);

        deleteHospital.delete(userId);

        hospitalRepository.save(deleteHospital);
    }
}
