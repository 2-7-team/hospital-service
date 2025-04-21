/*
package com._7.bookinghospital.hospital_service.presentation.dto.response;


import com._7.bookinghospital.hospital_service.domain.model.Schedule;
import lombok.Getter;

import java.time.LocalTime;
import java.util.UUID;

@Getter
public class CreateScheduleResponseDto {
    private String name; // 병원 이름
    private UUID id; // (병원) 일정 고유 식별자
    private LocalTime time;
    private Integer capacity;

    public static CreateScheduleResponseDto toResponse(Schedule schedule) {
        return new CreateScheduleResponseDto(schedule.getHospital().getName(), schedule.getId(), schedule.getTime(), schedule.getCapacity());
    }
}
 */
