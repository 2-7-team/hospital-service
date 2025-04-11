package com._7.bookinghospital.hospital_service.application.service;

import com._7.bookinghospital.hospital_service.domain.model.Hospital;
import com._7.bookinghospital.hospital_service.domain.model.Schedule;
import com._7.bookinghospital.hospital_service.domain.repository.HospitalRepository;
import com._7.bookinghospital.hospital_service.domain.repository.ScheduleRepository;
import com._7.bookinghospital.hospital_service.presentation.dto.request.CreateScheduleRequestDto;
import com._7.bookinghospital.hospital_service.presentation.dto.response.CreateScheduleResponseDto;
import com._7.bookinghospital.hospital_service.presentation.dto.response.FindOneScheduleResponseDto;
import jakarta.validation.Valid;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final HospitalRepository hospitalRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public CreateScheduleResponseDto create(UUID hospitalId, @Valid CreateScheduleRequestDto dto) {
        log.info("schedule service");
        // 1. 전달받은 hospitalId 존재 여부 확인
        // (예정) hospitalId 와 예외 메시지를 전달받는 메서드 작성, schedule 생성 및 조회 메서드에 적용
        if(!hospitalRepository.existsHospital(hospitalId)) {
            log.info("hospital does not exist");
         throw new NotFoundException("병원을 먼저 등록해주세요.");
        }

        // 2. 병원 객체 전달 받고,
        Hospital findHospital = hospitalRepository.findOneHospital(hospitalId);

        // 3. (예정) 중복 검사: 전달받은 시간대가 등록되어 있는지 확인

        // dto 로부터 전달받은 capacity 검사 및 설정: capacity 가 null 이라면 10 으로 설정
        dto.checkAndSetting();
        log.info("dto.checkAndSetting: {}", dto.getCapacity());

        // 4. dto 기반으로 db 에 전달할 entity 객체 생성
        Schedule schedule = Schedule.createScheduleBuilder()
                .hospital(findHospital)
                .capacity(dto.getCapacity())
                .time(dto.getTime())
                .build();

        log.info("schedule 테이블에 저장하기 전");
        // 5. (db) Schedule 테이블에 저장
        Schedule saved = scheduleRepository.save(schedule);
        log.info("schedule 테이블에 저장한 후");

        // 6. Hospital 객체의 scheduleList 에 추가
        findHospital.add(saved);

        // 7. 저장된 Schedule 객체를(saved) 가공해서 컨트롤러로 전달.
        return new CreateScheduleResponseDto(saved);
    }

    public FindOneScheduleResponseDto findOneScheduleByHospital(UUID hospitalId, UUID scheduleId) {
        log.info("schedule service - hospitalId: {}, scheduleId: {}", hospitalId, scheduleId);
        // 1. 전달받은 hospitalId 존재 여부 확인
        if(!hospitalRepository.existsHospital(hospitalId)) {
            throw new NotFoundException("병원이 존재하지 않습니다.");
        }

        // 2. 병원 존재 but 해당 병원이 schedule 을 등록하지 않았을 경우 예외 발생시키기
        // (의문) hospital repository 로 병원 스케쥴 데이터를 얻어야 하는가?
        Hospital findOneHospital = hospitalRepository.findOneHospital(hospitalId);

        // 2-1. 애그리거트 루트인 병원 도메인을 통해 해당 병원의 스케쥴 정보를 모두 가져와 schedule id 가 존재하는지 확인
        List<Schedule> schedules = findOneHospital.getSchedules();

        return schedules
                .stream()
                .filter(schedule -> schedule.getId().equals(scheduleId)) // Stream<Schedule>
                // (고민) schedule id 가 존재한다면, 당연히 하나밖에 존재하지 않을텐데, stream() 을 사용하는 것은 과한것인가?
                .findFirst()
                // map(schedule -> schedule.toFindOneScheduleResponseDto())
                .map(Schedule::toFindOneScheduleResponseDto) // Stream<FindOneScheduleResponseDto>
                .orElseThrow(() -> new NotFoundException("조회하신 스케쥴은 존재하지 않습니다."));
    }
}
