package com._7.bookinghospital.hospital_service.domain.model;

import bookinghospital.common_module.BaseEntity; // common_module 라이브러리에서 가져옴.
import com._7.bookinghospital.hospital_service.presentation.dto.response.FindOneScheduleResponseDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_schedules")
@ToString
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

    // (예정) 도메인 계층에서 presentation 의 dto 를 사용하는 것은 계층 위배, 추후 개선
    public FindOneScheduleResponseDto toFindOneScheduleResponseDto() {
        return new FindOneScheduleResponseDto (this.hospital.getName(), this.time, this.capacity);
    }

    // 운영시간대별 진료 가능 환자수 변경에 사용될 메서드
    public void updateCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void changeHospital(Hospital hospital) {
        this.hospital = hospital;
    }
}
