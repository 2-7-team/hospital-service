package com._7.bookinghospital.hospital_service.infrastructure.repository.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "reservation-service")
public interface ReservationFeignClient {
    /** JavaDoc 써봄.
     * 메서드 설명: 특정 병원의 특정 일정의 진료 가능 환자수[좌석수] 업데이트 메서드
     * 호출시 예약 서비스에 좌석수 업데이트 가능 여부 확인할 때 사용하는 메서드
     *
     * @param hospital_id: url 경로에 전달하는 병원 id
     * @param dto: RequestBody 로 전달되는 data, Map 에 담아 전달
     *           {
     *              "updateLeftSeat": 업데이트할 진료 가능 환자수[좌석수],
     *              "reservationTime": 기존 시간 정보
     *           }
     * @return 결과는 문자열(String)로 ResponseEntity 에 담겨 전달받음.
     */
    // url 경로로 전달할 데이터:
    // RequestBody 로 보낼 데이터: Integer updateLeftSeat, Integer reservationTime
    @PatchMapping("/api/reservations/internal/{hospital_id}")
    ResponseEntity<String> getResponse(@PathVariable UUID hospital_id,
                                  @RequestBody Map<String, Integer> dto);
}
