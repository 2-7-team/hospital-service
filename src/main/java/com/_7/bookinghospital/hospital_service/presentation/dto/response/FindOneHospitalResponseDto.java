package com._7.bookinghospital.hospital_service.presentation.dto.response;

import com._7.bookinghospital.hospital_service.domain.model.Hospital;
import lombok.Getter;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
// (의문) 필드마다 다음과 같은 경고문이 뜸: ex. 필드 'openHour'이(가) 'final'이 될 수 있습니다.
public class FindOneHospitalResponseDto {
    private UUID id; // 병원 식별 id

    private String name; // 병원명

    private String address; // 병원 주소(위치)

    private String phone; // 병원 전화번호

    private String description; // 병원 소개글

    private LocalTime openHour; // 병원 영업 오픈 시각

    private LocalTime closeHour; // 병원 영업 마감 시각

    // (예정) 신규 표시를 하기 위해 로직에 필요한 데이터
    protected LocalDateTime createdAt;

    // userId(유저 식별 id)
    protected Long createdBy;

    // (예정) 람다식으로 간단하게 나타낼 수 없을까? 정적 팩토리 메서드? 빌더 패턴?
    public FindOneHospitalResponseDto(Hospital hospital) {
        this.id = hospital.getId();
        this.name = hospital.getName();
        this.address = hospital.getAddress();
        this.phone = hospital.getPhone();
        this.description = hospital.getDescription();
        this.openHour = hospital.getOpenHour();
        this.closeHour = hospital.getCloseHour();
        this.createdAt = hospital.getCreatedAt();
        this.createdBy = hospital.getCreatedBy();
    }
}
