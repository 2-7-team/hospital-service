package com._7.bookinghospital.hospital_service.presentation.dto.request;

import com._7.bookinghospital.hospital_service.domain.model.Hospital;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@ToString
public class CreateScheduleRequestDto {
    @NotNull(message = "시간대 정보 입력은 필수입니다.")
    // time 의 값이 09:00 일 경우 오전 9~10 사이에 capacity 의 수만큼 병원에서 진료할 수 있다.
    private LocalTime time;

    // (조건) 클라이언트로부터 capacity 값을 전달받지 못할 때,
    // 기본 값으로(10명) 설정됨을 화면에서 병원 관계자에게 미리 공지함
    // 병원에서 진료 가능한 환자수
    private Integer capacity;

    public Integer checkAndSetting() {
        // 자동형변환 되겠지만 명시적으로 작성
        return capacity = (capacity == null)? (Integer) 10 : capacity;
    }
}
