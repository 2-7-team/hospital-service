package com._7.bookinghospital.hospital_service.infrastructure.repository.feign;

import org.springframework.cloud.openfeign.FeignClient;

// 리뷰 서비스에서 별점 갖고 오기 → 조회에 사용
@FeignClient(name = "review-service")
public interface ReviewFeignClient {
}
