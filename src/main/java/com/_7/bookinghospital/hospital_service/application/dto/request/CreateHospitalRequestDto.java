package com._7.bookinghospital.hospital_service.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@Builder
@ToString
public class CreateHospitalRequestDto {
        @NotBlank(message = "병원 이름은 필수입니다.")
        private String name;

        @NotBlank(message = "병원 주소는 필수입니다.")
        private String address;

        @NotBlank(message = "병원 전화번호는 필수입니다.")
        private String phone;

        @NotBlank(message = "병원 소개는 필수입니다.")
        private String description;

        @NotNull(message = "병원 영업 오픈 시간은 필수입니다.")
        private LocalTime openHour;

        @NotNull(message = "병원 영업 마감 시간은 필수입니다.")
        private LocalTime closeHour;
}
