package com._7.bookinghospital.hospital_service.presentation.dto.response;

import com._7.bookinghospital.hospital_service.domain.model.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@ToString
public class FindOneScheduleResponseDto {
    private String name; // 병원 이름
    private UUID id; // 병원 일정 스케쥴 식별 아이디
    private LocalTime time;
    private Integer capacity;

    public static FindOneScheduleResponseDto toResponse(Schedule schedule) {
        return new FindOneScheduleResponseDto (schedule.getHospital().getName(), schedule.getId(), schedule.getTime(), schedule.getCapacity());
    }
}
