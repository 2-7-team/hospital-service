package com._7.bookinghospital.hospital_service.application.service;

import bookinghospital.common_module.userInfo.UserDetails;
import com._7.bookinghospital.hospital_service.application.exception.DuplicateException;
import com._7.bookinghospital.hospital_service.application.exception.NotExistHospitalException;
import com._7.bookinghospital.hospital_service.domain.model.Hospital;
import com._7.bookinghospital.hospital_service.domain.repository.HospitalRepository;
import com._7.bookinghospital.hospital_service.infrastructure.repository.feign.ReviewFeignClient;
import com._7.bookinghospital.hospital_service.presentation.dto.request.CreateHospitalRequestDto;
import com._7.bookinghospital.hospital_service.presentation.dto.request.UpdateHospitalRequestDto;
import com._7.bookinghospital.hospital_service.presentation.dto.response.FindOneHospitalResponseDto;
import com._7.bookinghospital.hospital_service.presentation.dto.response.HospitalWithSchedulesResponse;
import com._7.bookinghospital.hospital_service.presentation.dto.response.UpdateHospitalResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class HospitalService {
    private final HospitalRepository hospitalRepository;
    private final ReviewFeignClient reviewFeignClient;

    @Transactional
    public UUID create(CreateHospitalRequestDto dto, UserDetails userDetails) throws AccessDeniedException {
        isHospitalRole(userDetails);
        isExistPhone(dto);
        Hospital hospital = dto.toEntity(userDetails.getUserId());
        Hospital saved = hospitalRepository.save(hospital);
        return saved.getId();
    }

    public FindOneHospitalResponseDto findOneHospital(UUID hospitalId) {
        log.info("hospitalId:{}",hospitalId);
        Hospital hospital = isActiveHospital(hospitalId);
        return getFindOneHospitalWithRating(hospital);
    }

    public Page<FindOneHospitalResponseDto> findAllHospitals(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Hospital> hospitalList = hospitalRepository.findAllHospitals(pageable);
        return hospitalList.map(this::getFindOneHospitalWithRating);
    }

    @Transactional
    public UpdateHospitalResponseDto updateHospitalInfo(UUID hospitalId, UpdateHospitalRequestDto updateHospitalInfo, UserDetails userDetails) throws AccessDeniedException {
        // 권한 확인
        String role = userDetails.getRole();
        Long userId = userDetails.getUserId();

        // 1. 업데이트할 병원 정보 존재하는지 고유 식별자(UUID hospitalId) 기반으로 병원 정보 확인하기
        Hospital findOneHospital = checkDbAndDelete(hospitalId);

        if(role.equals("ROLE_HOSPITAL") && findOneHospital.getUserId().equals(userId)) {
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
        } else {
            throw new AccessDeniedException("권한 불가로 해당 서비스에 접근할 수 없습니다.");
        }
    }

    @Transactional
    public void delete(UUID hospitalId, UserDetails userDetails) throws AccessDeniedException {
        Hospital deleteHospital = checkDbAndDelete(hospitalId);

        // 권한 확인
        Long userId = userDetails.getUserId();
        String role = userDetails.getRole();

        if(role.equals("ROLE_MASTER") ||
                (role.equals("ROLE_HOSPITAL") && deleteHospital.getUserId().equals(userId))) {
            deleteHospital.delete(userId);
            hospitalRepository.save(deleteHospital);
        } else {
            throw new AccessDeniedException("권한 불가로 해당 서비스에 접근할 수 없습니다.");
        }
    }

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
        List<Hospital> result = hospitalRepository.findAll();
        // List 가 반환됐지만 비어 있을 수 있음, 병원이 존재하는지 확인
        if(result.isEmpty()) {
            throw new NotExistHospitalException("등록된 병원이 존재하지 않습니다.");
        }

        // 2. 병원들은 모두 각 스케쥴 전부를 담아서 반환한다.
        // 스케쥴이 없는 병원의 경우 제외한다. filter 기능 사용
        return result
                .stream()
                .filter(hospital-> !hospital.getSchedules().isEmpty())
                .map(HospitalWithSchedulesResponse::new)
                .toList();
    }

    // ---------------------------------------------------------------------------------------

    public void isHospitalRole(UserDetails userDetails) throws AccessDeniedException{
        String role = userDetails.getRole();
        if(!role.equals("ROLE_HOSPITAL")) {
            throw new AccessDeniedException("권한 불가로 해당 서비스에 접근할 수 없습니다.");
        }
    }

    public void isExistPhone(CreateHospitalRequestDto dto) {
        String phoneNumber = dto.getPhone();
        if(hospitalRepository.existsByPhone(phoneNumber)) {
            throw new DuplicateException(phoneNumber+ " 은 이미 등록된 전화번호 입니다. 다른 번호를 등록해주세요.");
        }
    }

    public Hospital isActiveHospital(UUID hospitalId) {
        return hospitalRepository.isActiveHospital(hospitalId);
    }

    private FindOneHospitalResponseDto getFindOneHospitalWithRating(Hospital hospital) {
        UUID id = hospital.getId();
        Float averageRating = 0.0F;

        try {
            ResponseEntity<Float> response = reviewFeignClient.getResponse(id);
            if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                averageRating = response.getBody();
            } else {
                log.warn("hospitalId: {}, 리뷰 서비스 응답 실패, httpStatusCode: {}",
                        id,
                        response.getStatusCode().value()
                );
            }
        } catch (Exception e) {
            log.error("hospitalId: {}, 리뷰 서비스의 별점 조회 메서드 호출중 예외 발생", id);
            log.error("예외 발생 메시지: {}", e.getMessage());
        }
        return new FindOneHospitalResponseDto(hospital, averageRating);
    }
}
