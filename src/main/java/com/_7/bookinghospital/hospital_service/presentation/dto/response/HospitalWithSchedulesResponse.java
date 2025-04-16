package com._7.bookinghospital.hospital_service.presentation.dto.response;

// 계층 위반 발생

import com._7.bookinghospital.hospital_service.domain.model.Hospital;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// 내부적으로 리뷰 서비스가 호출할 때 사용되는 클래스
@Getter
public class HospitalWithSchedulesResponse {
    private UUID id; // 병원 식별 id
    private String name; // 병원명
    private String address; // 병원 주소(위치)
    private String phone; // 병원 전화번호
    private String description; // 병원 소개글
    private LocalTime openHour; // 병원 영업 오픈 시각
    private LocalTime closeHour; // 병원 영업 마감 시각
    // (의문) 계층 위반인데, response 폴더를 다시 application 계층으로 옮겨야 할까?
    private List<FindOneScheduleResponseDto> schedules = new ArrayList<>();
    protected LocalDateTime createdAt;
    // userId(유저 식별 id)
    protected Long createdBy;

    public HospitalWithSchedulesResponse(Hospital hospital) {
        this.id = hospital.getId();
        this.name = hospital.getName();
        this.address = hospital.getAddress();
        this.phone = hospital.getPhone();
        this.description = hospital.getDescription();
        this.openHour = hospital.getOpenHour();
        this.closeHour = hospital.getCloseHour();
        this.createdAt = hospital.getCreatedAt();
        this.createdBy = hospital.getCreatedBy();
        this.schedules = hospital.getSchedules()
                .stream()
                .map(FindOneScheduleResponseDto::toResponse)
                .toList();
    }

}
