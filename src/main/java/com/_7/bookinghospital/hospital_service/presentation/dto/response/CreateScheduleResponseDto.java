package com._7.bookinghospital.hospital_service.presentation.dto.response;

import com._7.bookinghospital.hospital_service.domain.model.Schedule;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class CreateScheduleResponseDto {
    private String name; // 병원 이름
    private LocalTime time;
    private Integer capacity;

    public CreateScheduleResponseDto(Schedule schedule) {
        this.name = schedule.getHospital().getName();
        this.time = schedule.getTime();
        this.capacity = schedule.getCapacity();
    }
}
