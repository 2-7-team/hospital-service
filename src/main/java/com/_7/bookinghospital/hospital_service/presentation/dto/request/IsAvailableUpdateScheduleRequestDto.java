package com._7.bookinghospital.hospital_service.presentation.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

// 예약 서비스에 병원 일정 업데이트시 전달할 dto
@Getter
@NoArgsConstructor
public class IsAvailableUpdateScheduleRequestDto {
    private UUID hospitalId;
    private UUID scheduleId;
    private Integer capacity;

    @Builder
    public IsAvailableUpdateScheduleRequestDto(UUID hospitalId, UUID scheduleId, Integer capacity) {
        this.hospitalId = hospitalId;
        this.scheduleId = scheduleId;
        this.capacity = capacity;
    }
}
