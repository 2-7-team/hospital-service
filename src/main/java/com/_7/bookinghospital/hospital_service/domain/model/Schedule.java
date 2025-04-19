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
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_schedule")
@Slf4j
public class Schedule extends BaseEntity {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @UuidGenerator
    @Column(nullable = false)
    private UUID id; // 특정 병원의 운영 시간대의 특정 시간대에 대한 정보를 식별할 수 있는 id

    @ManyToOne // PatientCapacityPerHours 과 Hospital 의 관계는 다대일
    @JoinColumn(name="hospital_id") // 외래키 컬럼 이름 지정
    private Hospital hospital;

    @Column(nullable = false)
    private LocalTime time; // time 의 값이 09:00 일 경우 오전 9~10 사이에 capacity 의 수만큼 병원에서 진료할 수 있다.

    @Column(nullable = false)
    private Integer capacity; // 병원에서 진료할 수 있는 환자수

    @Builder(builderMethodName = "createScheduleBuilder")
    public Schedule(Hospital hospital, LocalTime time, Integer capacity) {
        this.hospital = hospital;
        this.time = time;
        this.capacity = capacity;
    }

    // 운영시간대별 진료 가능 환자수 변경에 사용될 메서드
    public void updateCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void changeHospital(Hospital hospital) {
        log.info("changeHospital 첫 줄, hospital: {}", this.hospital.getId());
        this.hospital = hospital;
        log.info("hospital 대입후: {}", this.hospital.getId());
    }
}
