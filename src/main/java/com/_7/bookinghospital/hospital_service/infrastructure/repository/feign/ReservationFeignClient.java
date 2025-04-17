package com._7.bookinghospital.hospital_service.infrastructure.repository.feign;

import com._7.bookinghospital.hospital_service.presentation.dto.request.IsAvailableUpdateScheduleRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

// 병원 서비스(내 서비스)가 호출하고자 하는 다른 서비스를 FeignClient 인터페이스로 작성
// (체크 필요) 예약 서비스의 spring.application.name 이 reservation-service 가 맞는지 확인
@FeignClient(name = "reservation-service")
public interface ReservationFeignClient {
    // 호출하고자 하는 엔드 포인트 작성
    // 전달 데이터: hospitalId, scheduleId, updateCapacity
    @GetMapping// 엔드 포인트 작성할 것
    ResponseEntity<?> getResponse(@RequestBody IsAvailableUpdateScheduleRequestDto dto);
}
