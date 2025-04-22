package com._7.bookinghospital.hospital_service.presentation.controller;

import bookinghospital.common_module.userInfo.UserDetails;
import bookinghospital.common_module.userInfo.UserInfo;
import com._7.bookinghospital.hospital_service.application.service.HospitalService;
import com._7.bookinghospital.hospital_service.presentation.dto.request.CreateHospitalRequestDto;
import com._7.bookinghospital.hospital_service.presentation.dto.request.UpdateHospitalRequestDto;
import com._7.bookinghospital.hospital_service.presentation.dto.response.FindOneHospitalResponseDto;
import com._7.bookinghospital.hospital_service.presentation.dto.response.HospitalWithSchedulesResponse;
import com._7.bookinghospital.hospital_service.presentation.dto.response.UpdateHospitalResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hospitals")
@Slf4j
public class HospitalController {
    private final HospitalService hospitalService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateHospitalRequestDto dto,
                                    @UserInfo UserDetails userDetails) throws AccessDeniedException {
        UUID hospitalId = hospitalService.create(dto, userDetails);
        URI uri = UriComponentsBuilder.fromUriString("/{hospitalId}")
                .buildAndExpand(hospitalId)
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{hospitalId}")
    public ResponseEntity<FindOneHospitalResponseDto> findOneHospital(@PathVariable UUID hospitalId) {
        FindOneHospitalResponseDto findHospital = hospitalService.findOneHospital(hospitalId);
        return ResponseEntity.ok().body(findHospital);
    }

    @GetMapping
    public ResponseEntity<Page<FindOneHospitalResponseDto>> findAllHospitals(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Page<FindOneHospitalResponseDto> allHospitals = hospitalService.findAllHospitals(page, size);
        return ResponseEntity.ok().body(allHospitals);
    }

    // 병원 정보 수정하기 - 권한: 병원 관계자(해당 병원을 등록한 사람)
    @PatchMapping("/{hospitalId}")
    public ResponseEntity<UpdateHospitalResponseDto> updateHospitalInfo(@PathVariable UUID hospitalId,
                                                                        @RequestBody UpdateHospitalRequestDto updateDto,
                                                                        @UserInfo UserDetails userDetails) throws AccessDeniedException {
        UpdateHospitalResponseDto updateHospitalResponseDto = hospitalService.updateHospitalInfo(hospitalId, updateDto, userDetails);
        // 200 HttpStatusCode 와 함께 찾은 리소스를 반환함.
        // 리소스 변경 요청이 성공적으로 처리되었을 때, 클라이언트에 HttpStatusCode 로 200을 전달한다.
        return ResponseEntity.ok().body(updateHospitalResponseDto);
    }

    // 병원 삭제하기 (소프트 기능 구현)
    // 권한: MASTER, 병원 관계자(해당 병원을 등록한 사람)
    // (문제) updatedBy 에 userId 가 삽입 안됨.
    @DeleteMapping("/{hospitalId}")
    public ResponseEntity<Void> delete(@PathVariable UUID hospitalId,
                                       @UserInfo UserDetails userDetails) throws AccessDeniedException {
        hospitalService.delete(hospitalId, userDetails);
        // 삭제 요청이 성공적으로 이루어졌을 때 HttpStatus.NO_CONTENT(204) 를 반환함.
        return ResponseEntity.noContent().build();
    }

    // (완료) 리뷰 서비스에서 병원 존재 여부 확인하는 internal api 작성
    // (완료) 예외 처리
    // httpStatus: 404, 문자열 타입으로 "존재하지 않습니다." 반환
    @GetMapping("/internal/{hospitalId}")
    public ResponseEntity<Map<String, UUID>> checkHospital(@PathVariable UUID hospitalId,
                                                             HttpServletRequest request) {
        Map<String, UUID> response = new HashMap<>();

        String uri = request.getRequestURI();
        log.info("요청 uri: {}", uri);

        UUID uuid = hospitalService.checkHospital(hospitalId);

        response.put("hospitalId", uuid);
        return ResponseEntity.ok().body(response);
    }

    // 리뷰 서비스에서 요청할 모든 병원 정보와 각 병원이 등록한 모든 스케쥴
    @GetMapping("/internal/all")
    public ResponseEntity<List<HospitalWithSchedulesResponse>> findAllInfo(HttpServletRequest request) {
        String uri = request.getRequestURI();
        log.info("요청 uri: {}", uri );

        List<HospitalWithSchedulesResponse> response = hospitalService.findAllInfo();

        return ResponseEntity.ok().body(response);
    }

}
