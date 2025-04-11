package com._7.bookinghospital.hospital_service.presentation.dto.response;

import com._7.bookinghospital.hospital_service.domain.model.Hospital;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class UpdateHospitalResponseDto {
    private final String name; // 병원명
    private final String address; // 병원 주소(위치)
    private final String phone; // 병원 전화번호
    private final String description; // 병원 소개글
    private final LocalTime openHour; // 병원 영업 오픈 시각
    private final LocalTime closeHour; // 병원 영업 마감 시각

    public UpdateHospitalResponseDto(Hospital hospital) {
        this.name = hospital.getName();
        this.address = hospital.getAddress();
        this.phone = hospital.getPhone();
        this.description = hospital.getDescription();
        this.openHour = hospital.getOpenHour();
        this.closeHour = hospital.getCloseHour();
    }


}
