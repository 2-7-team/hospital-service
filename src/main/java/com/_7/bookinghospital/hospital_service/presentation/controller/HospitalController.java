package com._7.bookinghospital.hospital_service.presentation.controller;

import com._7.bookinghospital.hospital_service.application.service.HospitalService;
import com._7.bookinghospital.hospital_service.application.service.dto.request.CreateHospitalRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hospitals")
public class HospitalController {
    private final HospitalService hospitalService;

    // 특정 병원 등록하기, 권한: 병원 관계자
    @PostMapping
    public ResponseEntity<?> create(@Valid CreateHospitalRequestDto createHospitalRequestDto,
                                   BindingResult bindingResult) {
        return null;
    }
}
