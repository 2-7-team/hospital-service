//package com._7.bookinghospital.hospital_service.infrastructure.repository;
//
//import com._7.bookinghospital.hospital_service.domain.model.PatientCapacityPerHour;
//import com._7.bookinghospital.hospital_service.domain.repository.PatientCapacityPerHourRepository;
//import com._7.bookinghospital.hospital_service.infrastructure.repository.jpa.PatientCapacityPerHourJpaRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//@Repository
//@RequiredArgsConstructor
//public class PatientCapacityPerHourJpaRepositoryImpl implements PatientCapacityPerHourRepository {
//
//    private final PatientCapacityPerHourJpaRepository patientCapacityPerHourJpaRepository;
//
//    @Override
//    public PatientCapacityPerHour save(PatientCapacityPerHour patientCapacityPerHour) {
//        return patientCapacityPerHourJpaRepository.save(patientCapacityPerHour);
//    }
//}
