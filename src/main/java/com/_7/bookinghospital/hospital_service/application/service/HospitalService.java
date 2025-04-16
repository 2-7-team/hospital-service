package com._7.bookinghospital.hospital_service.application.service;

import com._7.bookinghospital.hospital_service.application.exception.NotExistHospitalException;
import com._7.bookinghospital.hospital_service.domain.model.Hospital;
import com._7.bookinghospital.hospital_service.domain.repository.HospitalRepository;
import com._7.bookinghospital.hospital_service.presentation.dto.request.CreateHospitalRequestDto;
import com._7.bookinghospital.hospital_service.presentation.dto.request.UpdateHospitalRequestDto;
import com._7.bookinghospital.hospital_service.presentation.dto.response.FindOneHospitalResponseDto;
import com._7.bookinghospital.hospital_service.presentation.dto.response.HospitalWithSchedulesResponse;
import com._7.bookinghospital.hospital_service.presentation.dto.response.UpdateHospitalResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
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
        Hospital hospital = Hospital.create(dto.getName(), dto.getAddress(), dto.getPhone(), dto.getDescription(), dto.getOpenHour(), dto.getCloseHour());

        Hospital saved = hospitalRepository.save(hospital);

        return saved.getId();
    }

    // 병원 단건 조회
    public FindOneHospitalResponseDto findOneHospital(UUID hospitalId) {
        // checkDbAndDelete(UUID id): db 에 병원이 존재하는지 && 소프트 삭제 됐는지
        Hospital findHospital = checkDbAndDelete(hospitalId);
        return new FindOneHospitalResponseDto(findHospital);
    }

    // 병원 목록 조회
    public Page<FindOneHospitalResponseDto> findAllHospitals(int page, int size) {
        // 페이지네이션
        int pageNo = (page != 0)? (page - 1): page;
        Pageable pageable = PageRequest.of(pageNo, size);

        Page<Hospital> hospitalList = hospitalRepository.findAllHospitals(pageable);
        return hospitalList.map(FindOneHospitalResponseDto::new);
    }

    @Transactional
    public UpdateHospitalResponseDto updateHospitalInfo(UUID hospitalId, UpdateHospitalRequestDto updateHospitalInfo) {
        // 1. 업데이트할 병원 정보 존재하는지 고유 식별자(UUID hospitalId) 기반으로 병원 정보 확인하기
        Hospital findOneHospital = checkDbAndDelete(hospitalId);

        // hospitalId 기반 병원 정보 존재시
        // 2. 병원 정보 수정 요청 값 유효성 검증하기
        Map<String, Object> extractUpdateFields = updateHospitalInfo.extractUpdateFields();

        // 2-1. 업데이트 요청 데이터가 하나도 전달되지 않았다면 예외 발생 시키기
        if(extractUpdateFields.isEmpty()) {
            throw new IllegalArgumentException("수정할 데이터가 없습니다. 수정할 항목을 다시 확인해주세요.");
        }

        // key-value (entry) Set 에 담기
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

        Hospital deleteHospital = checkDbAndDelete(hospitalId);

        deleteHospital.delete(userId);

        hospitalRepository.save(deleteHospital);
    }

    // checkDbAndDelete(UUID id): db 에 병원이 존재하는지 && 소프트 삭제 됐는지
    private Hospital checkDbAndDelete(UUID hospitalId) {
        // db 에 병원 정보 존재 여부
        Hospital findHospital = hospitalRepository.findByHospitalId(hospitalId)
                .orElseThrow(() -> new NotExistHospitalException("조회하신 병원은 존재하지 않습니다."));

        // 병원이 소프트 삭제 됐는지 확인
        if(findHospital.isDeleted()) {
            throw new NotExistHospitalException("조회하신 병원은 존재하지 않습니다.");
        }
        return findHospital;
    }

    public UUID checkHospital(UUID hospitalId) {
        Hospital hospital = checkDbAndDelete(hospitalId);
        return hospital.getId();
    }

    @Transactional
    public List<HospitalWithSchedulesResponse> findAllInfo() {
        // 1. 병원 목록 존재하는지 먼저 확인
        Optional<List<Hospital>> result = hospitalRepository.findAll();
        if(result.isEmpty()) {
            // Optional 객체가 감싼게 없다면, null: 병원 목록 자체가 반환되지 않음(List 자체가 반환되지 않음)
            // (의문) 병원 목록 자체가 반환되지 않았다는 것이 병원 테이블이 존재하지 않는다는 것인가?
            log.info("Optional.empty");
            throw new NotExistHospitalException("등록된 병원이 존재하지 않습니다.");
        }
        // List 가 반환됐지만 비어 있을 수 있음, 병원이 존재하는지 확인
        // (의문) Optional 객체 안에 List 타입이 없는 거랑 List 타입이 반환됐지만 List 가 비어있는 경우가 어떤 경우인지?
        List<Hospital> hospitals = result.get();
        if(hospitals.isEmpty())
            throw new NotExistHospitalException("등록된 병원이 존재하지 않습니다.");

        // 2. 병원들은 모두 각 스케쥴 전부를 담아서 반환한다.
        // 스케쥴이 없는 병원의 경우 제외한다. filter 기능 사용
        return hospitals.stream()
                .filter(hospital-> !hospital.getSchedules().isEmpty())
                .map(HospitalWithSchedulesResponse::new)
                .toList();
    }
}
