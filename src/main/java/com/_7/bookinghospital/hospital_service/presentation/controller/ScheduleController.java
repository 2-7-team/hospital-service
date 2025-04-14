package com._7.bookinghospital.hospital_service.presentation.controller;

import com._7.bookinghospital.hospital_service.presentation.dto.request.CreateScheduleRequestDto;
import com._7.bookinghospital.hospital_service.application.service.ScheduleService;
import com._7.bookinghospital.hospital_service.presentation.dto.response.CreateScheduleResponseDto;
import com._7.bookinghospital.hospital_service.presentation.dto.response.FindOneScheduleResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule/{hospitalId}")
@Slf4j
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<CreateScheduleResponseDto> create(@PathVariable UUID hospitalId,
                                    @Valid @RequestBody CreateScheduleRequestDto dto,
                                    BindingResult bindingResult) {
        log.info("schedule create POST - dto : {}", dto);
        CreateScheduleResponseDto responseDto = scheduleService.create(hospitalId, dto);
        // 리소스 생성이므로 HttpStatusCode 는 HttpStatus.CREATED 를 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 특정 병원의 특정 운영시간대의 진료 가능 환자수 정보를 담은 (하나의) 행을 반환한다.
    @GetMapping ("/{scheduleId}")// default: /api/schedule/{hospitalId}
    public ResponseEntity<?> findOneScheduleByHospital(@PathVariable UUID hospitalId,
                                                       @PathVariable UUID scheduleId) {
        log.info("schedule controller - hospitalId: {}, scheduleId: {}", hospitalId, scheduleId);
        FindOneScheduleResponseDto findScheduleByHospital = scheduleService.findOneScheduleByHospital(hospitalId, scheduleId);
        return ResponseEntity.ok().body(findScheduleByHospital);
    }

    // 특정 병원의 운영시간대별 진료 가능 환자수 정보를 담은 모든 행을 반환한다.
    @GetMapping// default: /api/schedule/{hospitalId}
    public ResponseEntity<List<FindOneScheduleResponseDto>> findAllSchedules(@PathVariable UUID hospitalId) {
        List<FindOneScheduleResponseDto> schedules = scheduleService.findAllSchedules(hospitalId);
        return ResponseEntity.ok().body(schedules);
    }

    // 특정 병원의 특정 운영시간대의 진료 가능한 환자수(좌석수) 정보만 변경이 가능하다.
    // 권한: 병원 관계자
    @PatchMapping("/{scheduleId}")
    public ResponseEntity<FindOneScheduleResponseDto> updateSchedule(@PathVariable UUID hospitalId,
                                            @PathVariable UUID scheduleId,
                                            @RequestBody Map<String, Integer> request) {
        // 필드 한 개만 전달받기 때문에 Map 타입을 사용함.
        log.info("request.get('capacity'): {}", request.get("capacity"));
        FindOneScheduleResponseDto updated = scheduleService.updateSchedule(hospitalId, scheduleId, request.get("capacity"));
        return ResponseEntity.ok().body(updated);
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> delete(@PathVariable UUID hospitalId,
                                    @PathVariable UUID scheduleId) {
        scheduleService.delete(hospitalId, scheduleId);
        return ResponseEntity.noContent().build();
    }

}
