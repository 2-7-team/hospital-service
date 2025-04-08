package com._7.bookinghospital.hospital_service.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_hospital")
public class Hospital extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @UuidGenerator
    @Column(nullable = false)
    private UUID id; // 병원 식별 id

    @Column(nullable = false)
    private String name; // 병원명

    @Column(nullable = false)
    private String address; // 병원 주소(위치)

    @Column(nullable = false)
    private String phone; // 병원 전화번호

    @Column(nullable = false)
    private String description; // 병원 소개글

    @Column(nullable = false)
    private LocalTime openHour; // 병원 영업 오픈 시각

    @Column(nullable = false)
    private LocalTime closeHour; // 병원 영업 마감 시각

    @Builder(builderMethodName = "createHospitalBuilder")
    public Hospital(String name, String address, String phone, String description, LocalTime openHour, LocalTime closeHour) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.description = description;
        this.openHour = openHour;
        this.closeHour = closeHour;
    }



}