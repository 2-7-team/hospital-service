package com._7.bookinghospital.hospital_service.application.service;

import bookinghospital.common_module.userInfo.UserDetails;
import com._7.bookinghospital.hospital_service.application.exception.DuplicateException;
import com._7.bookinghospital.hospital_service.application.exception.NotExistHospitalException;
import com._7.bookinghospital.hospital_service.domain.model.Hospital;
import com._7.bookinghospital.hospital_service.domain.model.Schedule;
import com._7.bookinghospital.hospital_service.domain.repository.HospitalRepository;
import com._7.bookinghospital.hospital_service.domain.repository.ScheduleRepository;
import com._7.bookinghospital.hospital_service.infrastructure.repository.feign.ReservationFeignClient;
import com._7.bookinghospital.hospital_service.presentation.dto.request.CreateScheduleRequestDto;
import com._7.bookinghospital.hospital_service.presentation.dto.response.CreateScheduleResponseDto;
import com._7.bookinghospital.hospital_service.presentation.dto.response.FindOneScheduleResponseDto;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ScheduleService {
    private final HospitalRepository hospitalRepository;
    private final ScheduleRepository scheduleRepository;
    private Long userId = 1L; // 임의의 유저 정보

    private final ReservationFeignClient reservationFeignClient;

    public CreateScheduleResponseDto create(UUID hospitalId,
                                            CreateScheduleRequestDto dto,
                                            UserDetails userDetails) throws AccessDeniedException {
        log.info("schedule service");
        // 1. 전달받은 hospitalId 존재 여부 && 소프트 삭제 여부 확인
        Hospital findHospital = checkDbAndDeleteHospital(hospitalId);

        // 권한 확인
        Long userId = userDetails.getUserId();
        String role = userDetails.getRole();

        if(role.equals("ROLE_HOSPITAL") && findHospital.getUserId().equals(userId)) {
            // 3. (완료) 중복 검사: 일정 정보가 있는지 먼저 확인(빈 리스트면 통과),
            // 일정 정보가 존재하면 전달받은 시간대가 등록되어 있는지 확인
            List<Schedule> schedules = findHospital.getSchedules();

            if(!schedules.isEmpty()) {
                schedules.stream()
                        .filter(schedule -> schedule.getTime().equals(dto.getTime()))
                        .findAny() // 클라이언트로부터 전달받은 시간과 db 에 저장된 시간이 일치하는 행이 하나라도 있다면,
                        .ifPresent(matched -> {
                            throw new DuplicateException("이미 등록되어 있는 시간입니다.");
                        });
            }

            // dto 로부터 전달받은 capacity 검사 및 설정: capacity 가 null 이라면 10 으로 설정
            dto.checkAndSetting();
            log.info("dto.checkAndSetting: {}", dto.getCapacity());

            // 4. dto 기반으로 db 에 전달할 entity 객체 생성
            Schedule schedule = Schedule.createScheduleBuilder()
                    .hospital(findHospital)
                    .capacity(dto.getCapacity())
                    .time(dto.getTime())
                    .build();

        /*
            버전1: 병원 레파지토리를 통해 병원 스케쥴을 등록한다.
            findHospital.add(schedule);
            hospitalRepository.save(findHospital);
            return new CreateScheduleResponseDto(schedule);
        */

        /*
            버전2: 스케쥴 레포지토리를 통해 스케쥴을 등록한다.
            Schedule saved = scheduleRepository.save(schedule); // 일정에 저장
            findHospital.add(saved);

            // 7. 저장된 Schedule 객체를(saved) 가공해서 컨트롤러로 전달.
            return new CreateScheduleResponseDto(saved);
        */

            Schedule saved = scheduleRepository.save(schedule); // 일정에 저장

            // 해당 병원에서 방금 저장된 일정이 존재하는지 등의 어떤 로직들이 다음에 진행되는 것이 아니라
            // 방금 저장된 일정을 반환하는 것이 끝인데, 해당 병원에 일정을 추가하는 것이 의미있는가?
            findHospital.add(saved);

            // 7. 저장된 Schedule 객체를(saved) 가공해서 컨트롤러로 전달.
            return new CreateScheduleResponseDto(saved);
        } else {
            throw new AccessDeniedException("권한불가로 해당 서비스에 접근할 수 없습니다.");
        }


    }

    public FindOneScheduleResponseDto findOneScheduleByHospital(UUID hospitalId, UUID scheduleId) {
        log.info("schedule service - hospitalId: {}, scheduleId: {}", hospitalId, scheduleId);
        // 1. 전달받은 hospitalId 존재 여부 && 소프트 삭제 여부 확인
        Hospital findHospital = checkDbAndDeleteHospital(hospitalId);

        // 2. 병원 존재 but 해당 병원이 schedule 이 아예 없을 경우 예외 발생
        List<Schedule> schedules = checkScheduleExist(findHospital);

        return schedules
                .stream()
                // 클라이언트로부터 전달받은 일정 식별자가 db 에 존재하는지, 그리고 존재할 경우 (소프트) 삭제 여부도 확인
                .filter(schedule -> schedule.getId().equals(scheduleId) && !schedule.isDeleted()) // Stream<Schedule>
                .findAny()
                .map(FindOneScheduleResponseDto::toResponse) // Stream<FindOneScheduleResponseDto>
                .orElseThrow(() -> new NotFoundException("조회하신 스케쥴은 존재하지 않습니다."));
    }

    // 특정 병원의 모든 일정 조회
    public List<FindOneScheduleResponseDto> findAllSchedules(UUID hospitalId) {
        // 1. 전달받은 hospitalId 존재 여부 && 소프트 삭제 여부 확인
        Hospital findHospital = checkDbAndDeleteHospital(hospitalId);

        // 2. 병원 존재 but 해당 병원이 schedule 이 아예 없을 경우
        // 2-1. 애그리거트 루트인 병원 도메인을 통해 해당 병원의 스케쥴 정보를 모두 가져오기
        // 일차적으로 해당 병원이 등록한 스케쥴 정보가 있는지 확인
        List<Schedule> schedules = checkScheduleExist(findHospital);

        return schedules
                .stream()
                // 스케쥴중 (소프트) 삭제 여부 확인, schedule.isDeleted() 가 false 인 schedule 만
                // FindOneScheduleResponseDto 타입으로 변환해서 반환하기
                .filter(schedule -> !schedule.isDeleted())
                .map(FindOneScheduleResponseDto::toResponse)
                .toList();
    }

    private static List<Schedule> checkScheduleExist(Hospital findHospital) {
        List<Schedule> schedules = findHospital.getSchedules();

        if(schedules.isEmpty()) {
            throw new NotFoundException("시간대별 운영 정보가 존재하지 않습니다.");
        }
        return schedules;
    }

    // 특정 병원의 특정 일정 업데이트 하기
    public FindOneScheduleResponseDto updateSchedule(UUID hospitalId, UUID scheduleId, Integer capacity, UserDetails userDetails) throws AccessDeniedException {
        // 1. 전달받은 hospitalId 존재 여부 && 소프트 삭제 여부 확인
        Hospital findHospital = checkDbAndDeleteHospital(hospitalId);
        log.info("삭제 여부: {}", findHospital.isDeleted());

        // 권한 확인
        Long userId = userDetails.getUserId();
        String role = userDetails.getRole();
        
        if(role.equals("ROLE_HOSPITAL") && findHospital.getUserId().equals(userId)) {
            // 2. 병원 존재 but 해당 병원이 update 할 schedule id 가 없을 때(소프트 삭제 여부도 확인),
            Schedule findSchedule = findHospital
                    // List<Schedule> 빈 리스트일 수 있음.
                    // 비어있는 리스트에서 stream() 을 호출하고, filter() 를 사용하는 것으로 에러가 발생하지 않음.
                    .getSchedules()
                    .stream()
                    .filter(schedule -> schedule.getId().equals(scheduleId) && !schedule.isDeleted())
                    .findAny() // Optional<Schedule>
                    .orElseThrow(() -> new NotFoundException("업데이트할 병원 스케쥴 정보가 존재하지 않습니다."));

            // 3. 클라이언트로부터 전달받은 데이터인 진료 가능 환자수(좌석수) 가 null 이거나 0 보다 작을 경우 예외를 발생시킨다.
            if(capacity == null || capacity <= 0) {
                throw new IllegalArgumentException("진료 가능 환자수(좌석수)를 다시 확인해주세요.");
            }

            // 4. (예정)예약 서비스로부터 예약 상태를 확인하여 클라이언트로부터 전달받은
            // 진료 가능 환자수(좌석수) 정보를 업데이트할 수 있는지 체크한다.
            // feignClient 호출 --- 이 부분 로직은 예약 서비스와 논의중

            if(findSchedule.getCapacity().equals(capacity)) {
                throw new RuntimeException("변경하려는 진료 가능 환자 수(좌석수)가 기존과 동일하여 업데이트된 사항이 없습니다.");
            }

            findSchedule.updateCapacity(capacity);
            findHospital.add(findSchedule);
            Hospital updated = hospitalRepository.save(findHospital);

            // (의문) hospitalRepository 를 통해서 schedule 테이블에 접근하고, crud 를 진행해야 하는가?
            // (문제) 그렇다면 아래와 같이 코드가 길어질 수 밖에 없다.
            Schedule updatedSchedule = updated.getSchedules()
                    .stream()
                    .filter(schedule -> schedule.getId().equals(scheduleId))
                    .findAny() // Optional<Schedule>
                    .orElseThrow(() -> new NotFoundException("병원 일정을 다시 확인해주세요."));

            log.info("업데이트된 병원 일정 - 병원 id: {}, 병원 일정 id: {}, 일정 시간: {}, 일정 수용 환자수: {}", updatedSchedule.getHospital().getId(),
                    updatedSchedule.getId(), updatedSchedule.getTime(), updatedSchedule.getCapacity());

            return FindOneScheduleResponseDto.toResponse(updatedSchedule);
        } else {
            throw new AccessDeniedException("권한 불가로 해당 서비스에 접근이 불가합니다.");   
        }
    }

    // 특정 병원의 특정 일정 (소프트) 삭제하기
    public void delete(UUID hospitalId, UUID scheduleId, UserDetails userDetails)
            throws AccessDeniedException {
        // 1. 전달받은 hospitalId 존재 여부 && 소프트 삭제 여부 확인
        Hospital findHospital = checkDbAndDeleteHospital(hospitalId);
        
        // 권한 확인
        String role = userDetails.getRole();
        Long userId = userDetails.getUserId();

        if(role.equals("ROLE_MASTER") || (role.equals("ROLE_HOSPITAL") && findHospital.getUserId().equals(userId))) {
            // 해당 병원에 등록된 일정이 하나도 없을 때: 삭제할 일정이 없다는 것
            if(findHospital.getSchedules().isEmpty()) {
                throw new NotFoundException(findHospital.getName()+ "에 등록된 일정이 없습니다.");
            }

            // 해당 병원에 전달한 scheduleId 와 일치하는 일정이 없을 때 예외 발생
            Schedule findSchedule = findHospital.getSchedules()
                    .stream()
                    .filter(schedule -> schedule.getId().equals(scheduleId))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("요청하신 일정 정보가 존재하지 않아 삭제가 불가합니다."));

            findSchedule.delete(userId);
            findHospital.add(findSchedule);
            Hospital updated = hospitalRepository.save(findHospital);
        } else {
            throw new AccessDeniedException("권한 불가로 해당 서비스를 이용하실 수 없습니다.");
        }
    }

    // checkDbAndDelete(UUID id): db 에 병원이 존재하는지 && 소프트 삭제 됐는지
    private Hospital checkDbAndDeleteHospital(UUID hospitalId) {
        // db 에 병원 정보 존재 여부
        Hospital findHospital = hospitalRepository.findByHospitalId(hospitalId)
                .orElseThrow(() -> new NotExistHospitalException("조회하신 병원은 존재하지 않습니다."));

        // 병원이 소프트 삭제 됐는지 확인
        if(findHospital.isDeleted()) {
            throw new NotExistHospitalException("조회하신 병원은 존재하지 않습니다.");
        }
        return findHospital;
    }
}
