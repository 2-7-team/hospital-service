package com._7.bookinghospital.hospital_service.presentation.controller;

import com._7.bookinghospital.hospital_service.application.service.HospitalService;
import com._7.bookinghospital.hospital_service.application.dto.request.CreateHospitalRequestDto;
import com._7.bookinghospital.hospital_service.application.dto.request.UpdateHospitalRequestDto;
import com._7.bookinghospital.hospital_service.application.dto.response.FindOneHospitalResponseDto;
import com._7.bookinghospital.hospital_service.application.dto.response.UpdateHospitalResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hospitals")
@Slf4j
public class HospitalController {
    private final HospitalService hospitalService;

    // 병원 등록하기, 권한: 병원 관계자
    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody CreateHospitalRequestDto dto,
                                   BindingResult bindingResult) {
        log.info("병원등록 - create(), dto: {}", dto.toString());
        // 1. (예정) dto 유효성 검증: 공통 모듈에 존재하는 전역 예외에 dto 유효성 검증시 발생 예외를 처리하는 핸들러 및 예외가 있는가
        // 2. (완료) dto 저장
        String savedHospitalName = hospitalService.create(dto);
        // 3. (완료) 리소스가 성공적으로 생성되어서 201과 생성된 병원 정보(리소스 가공)를 반환하기
        // 4. (예정) 추후 생성된 병원 정보를 볼 수 있는(조회하는) uri 전달하기.
        return new ResponseEntity<String>(savedHospitalName, HttpStatus.CREATED);
    }

    // 병원 정보 단건 조회 - 권한: ALL
    @GetMapping("/{hospitalId}") // (의문) @Pathvariable 로 받는 매개변수명이 카멜케이스여도 되는가?
    public ResponseEntity<FindOneHospitalResponseDto> findOneHospital(@PathVariable UUID hospitalId) {
        FindOneHospitalResponseDto findHospital = hospitalService.findOneHospital(hospitalId);
        // 200 HttpStatusCode 와 함께 찾은 리소스를 반환함.
        return ResponseEntity.ok().body(findHospital);
    }

    // 병원 목록 조회 - 권한: ALL
    @GetMapping
    public ResponseEntity<List<FindOneHospitalResponseDto>> findAllHospitals() {
        List<FindOneHospitalResponseDto> allHospitals = hospitalService.findAllHospitals();
        return ResponseEntity.ok().body(allHospitals);
    }

    // 병원 정보 수정하기 - 권한: 병원 관계자(해당 병원을 등록한 사람)
    @PatchMapping("/{hospitalId}") // (의문) @Pathvariable 로 받는 매개변수명이 카멜케이스여도 되는가?
    public ResponseEntity<UpdateHospitalResponseDto> updateHospitalInfo(@PathVariable UUID hospitalId,
                                                                        @RequestBody UpdateHospitalRequestDto updateDto) {
        UpdateHospitalResponseDto updateHospitalResponseDto = hospitalService.updateHospitalInfo(hospitalId, updateDto);
        // 200 HttpStatusCode 와 함께 찾은 리소스를 반환함.
        // 리소스 변경 요청이 성공적으로 처리되었을 때, 클라이언트에 HttpStatusCode 로 200을 전달한다.
        return ResponseEntity.ok().body(updateHospitalResponseDto);
    }

    // 병원 삭제하기 (소프트 기능 구현)
    // 권한: MASTER, 병원 관계자(해당 병원을 등록한 사람)
    // (문제) updatedBy 에 userId 가 삽입 안됨.
    @DeleteMapping("/{hospitalId}")
    public ResponseEntity<?> delete(@PathVariable UUID hospitalId) {
        hospitalService.delete(hospitalId);
        // 삭제 요청이 성공적으로 이루어졌을 때 HttpStatus.NO_CONTENT(204) 를 반환함.
        return ResponseEntity.noContent().build();
    }
}
