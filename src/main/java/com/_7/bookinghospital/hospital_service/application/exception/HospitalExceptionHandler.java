package com._7.bookinghospital.hospital_service.application.exception;

import bookinghospital.common_module.handler.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
@Slf4j
public class HospitalExceptionHandler extends GlobalExceptionHandler {
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        // AccessDeniedException 은 보통 403 FORBIDDEN 상태 코드와 함께 반환
        log.info("AccessDeniedException");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler({DuplicateException.class})
    public ResponseEntity<String> handleDuplicationException(DuplicateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({NotExistHospitalException.class})
    public ResponseEntity<String> handleNotExistHospitalException(NotExistHospitalException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
