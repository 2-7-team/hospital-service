package com._7.bookinghospital.hospital_service.presentation.controller;

import com._7.bookinghospital.hospital_service.application.service.PatientCapacityPerHourService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PatientCapacityPerHourController {
    private final PatientCapacityPerHourService patientCapacityPerHourService;
}
