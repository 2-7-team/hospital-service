package com._7.bookinghospital.hospital_service.presentation.dto.response;

import com._7.bookinghospital.hospital_service.domain.model.Hospital;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class FindOneScheduleResponseDto {
    private String name; // 병원 이름
    private LocalTime time;
    private Integer capacity;
}
