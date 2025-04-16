package com._7.bookinghospital.hospital_service.application.exception;

// (의문) RuntimeException 과 Exception 의 차이
// 병원이 존재하지 않을 때 발생하는 예외
// 예시: 병원이 소프트 삭제됐을 때, 클라이언트가 전달한 id 를 기반으로 db 조회시 해당 병원이 없을 때
public class NotExistHospitalException extends RuntimeException {
    public NotExistHospitalException(String message) {
        super(message);
    }
}
