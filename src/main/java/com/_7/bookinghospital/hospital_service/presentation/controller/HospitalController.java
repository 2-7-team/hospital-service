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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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

    // 병원 등록하기, 권한: 병원 관계자
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateHospitalRequestDto dto,
                                    BindingResult result,
                                    @UserInfo UserDetails userDetail) {
        log.info("병원등록 - create(), dto: {}", dto);
        log.info("userId: {}, role: {}", userDetail.getUserId(), userDetail.getRole());

        Map<String, String> dtoValid = dto.isValid(result);

        if(!dtoValid.isEmpty()) {
            return ResponseEntity.badRequest().body(dtoValid);
        }

        // 2. (완료) dto 저장
        UUID hospitalId = hospitalService.create(dto);

        URI uri = UriComponentsBuilder.fromUriString("/{hospitalId}")
                .buildAndExpand(hospitalId)
                .toUri();
        log.info("uri: {}", uri);

        // 3. (완료) 리소스가 성공적으로 생성되어서 201과 생성된 병원 정보(리소스 가공)를 반환하기
        // 4. (완료) 생성된 병원 정보를 조회하는 uri 클라이언트에 전달.
        //     : header 에 key 가 Location, value 가 저장된 병원의 id 값을 담아서 클라이언트에 반환됨 → 포스트 맨으로 확인 완료
        // 5. (예정) 테스트 코드 작성
        return ResponseEntity.created(uri).build();
    }

    // 병원 정보 단건 조회 - 권한: ALL
    @GetMapping("/{hospitalId}")
    public ResponseEntity<FindOneHospitalResponseDto> findOneHospital(@PathVariable UUID hospitalId) {
        FindOneHospitalResponseDto findHospital = hospitalService.findOneHospital(hospitalId);
        // 200 HttpStatusCode 와 함께 찾은 리소스를 반환함.
        return ResponseEntity.ok().body(findHospital);
    }

    // 병원 목록 조회 - 권한: ALL
    @GetMapping // /api/hospitals?page=1&size=10&search=검색어
    public ResponseEntity<Page<FindOneHospitalResponseDto>> findAllHospitals(
            // 클라이언트가 선택한 페이지 번호
            @RequestParam(required = false, defaultValue = "0") int page,
            // 한 페이지에 보여줄 병원 정보 수, 10개가 기본 값
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        log.info("page: {}, size: {}", page, size);
        Page<FindOneHospitalResponseDto> allHospitals = hospitalService.findAllHospitals(page, size);
        return ResponseEntity.ok().body(allHospitals);
    }

    // 병원 정보 수정하기 - 권한: 병원 관계자(해당 병원을 등록한 사람)
    @PatchMapping("/{hospitalId}")
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
    public ResponseEntity<Void> delete(@PathVariable UUID hospitalId) {
        hospitalService.delete(hospitalId);
        // 삭제 요청이 성공적으로 이루어졌을 때 HttpStatus.NO_CONTENT(204) 를 반환함.
        return ResponseEntity.noContent().build();
    }

    // (완료) 리뷰 서비스에서 병원 존재 여부 확인하는 internal api 작성
    // (예정) 예외 처리
    // httpStatus: 404, {error: "존재하지 않습니다."}
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

    // (예정) 등록된 병원 정보의 전체 스케쥴
    // 리뷰 서비스에서 요청할 모든 병원 정보와 각 병원이 등록한 모든 스케쥴
    @GetMapping("/internal/all")
    public ResponseEntity<List<HospitalWithSchedulesResponse>> findAllInfo(HttpServletRequest request) {
        String uri = request.getRequestURI();
        log.info("요청 uri: {}", uri );

        List<HospitalWithSchedulesResponse> response = hospitalService.findAllInfo();

        return ResponseEntity.ok().body(response);
    }

}
