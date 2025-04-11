package com._7.bookinghospital.hospital_service.infrastructure.repository;

import com._7.bookinghospital.hospital_service.domain.model.Schedule;
import com._7.bookinghospital.hospital_service.domain.repository.ScheduleRepository;
import com._7.bookinghospital.hospital_service.infrastructure.repository.jpa.ScheduleJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ScheduleRepositoryAdapter implements ScheduleRepository {

    @Lazy
    private final ScheduleJpaRepository scheduleJpaRepository;

    @Override
    public Schedule save(Schedule schedule) {
        log.info("infrastructure>ScheduleRepositoryAdapter - save 메서드 안");
        return scheduleJpaRepository.save(schedule);
    }
}
