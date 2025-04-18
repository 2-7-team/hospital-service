package com._7.bookinghospital.hospital_service.domain.model;

import bookinghospital.common_module.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalTime;
import java.util.*;

@Entity
@Getter
@Table(name = "p_hospital")
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hospital extends BaseEntity {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @UuidGenerator
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

    // cascade = CascadeType.REMOVE: 병원이 삭제 되면 병원과 관련된 운영 시간대별 환자 진료 가능 데이터(row)들도 모두 사라지도록 설정
    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    List<Schedule> schedules = new ArrayList<>();

    @Column(nullable = false)
    private Long userId; // createdBy 와 userId

    // 정적 팩토리 메서드
    public static Hospital create(String name, String address, String phone, String description, LocalTime openHour, LocalTime closeHour, Long userId) {
        return new Hospital(name, address, phone, description, openHour, closeHour, userId);
    }

    @Builder
    private Hospital(String name, String address, String phone, String description, LocalTime openHour, LocalTime closeHour, Long userId) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.description = description;
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.createdBy = userId;
        this.userId = userId;
    }

    public Hospital exchangeInfo(Set<Map.Entry<String, Object>> entries) {

        for(Map.Entry<String, Object> entry : entries) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // address, phone, description, openHour, closeHour
            // Objects.equals() 로 비교하면 null 또한 체크할 수 있음.
            // (의문) updateHospitalInfo.extractUpdateFields() 메서드로 이미 null 체크를 진행했는데,
            // 또 null 체크를 강화할 필요가 있는가?
            if(key.equals("address") && !Objects.equals(value, this.address)) {
                this.address = (String) value;
            }

            // phone
            if (key.equals("phone") && !Objects.equals(value, this.phone)) {
                this.phone = (String) value;
            }

            // description
            if (key.equals("description") && !Objects.equals(value, this.description)) {
                this.description = (String) value;
            }

            // openHour
            if (key.equals("openHour") && !Objects.equals(value, this.openHour)) {
                this.openHour = (LocalTime) value;
            }

            // closeHour
            if (key.equals("closeHour") && !Objects.equals(value, this.closeHour)) {
                this.closeHour = (LocalTime) value;
            }
        }

        return this;
    }

    public void add(Schedule schedule) {
        // schedule.changeHospital(this);
        this.schedules.add(schedule);
    }
}