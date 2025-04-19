package com._7.bookinghospital.hospital_service.infrastructure.repository.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

// 리뷰 서비스에서 별점 갖고 오기 → 조회에 사용
@FeignClient(name = "review-service")
public interface ReviewFeignClient {
    /**
     * 해당 병원의 별점을 조회하기
     * @param hospitalId: url 경로로 UUID 타입의 병원 고유 식별자를(hospitalId) 전달한다.
     * @return Float 타입의 데이터가 ResponseEntity 에 담겨 반환된다.
     */
    @GetMapping("/api/reviews/{hospitalId}/rating")
    ResponseEntity<Float> getResponse(@PathVariable UUID hospitalId);
}
