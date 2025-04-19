package com._7.bookinghospital.hospital_service.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@ToString
@Slf4j
public class CreateHospitalRequestDto {
        @NotBlank(message = "병원 이름은 필수입니다.")
        private String name;

        @NotBlank(message = "병원 주소는 필수입니다.")
        private String address;

        @NotBlank(message = "병원 전화번호는 필수입니다.")
        private String phone;

        @NotBlank(message = "병원 소개는 필수입니다.")
        private String description;

        @NotNull(message = "병원 영업 오픈 시간은 필수입니다.")
        private LocalTime openHour;

        @NotNull(message = "병원 영업 마감 시간은 필수입니다.")
        private LocalTime closeHour;

        // Map 타입이라 Optional 로 감쌀 필요가 없음.
        // 필드의 유효성 검사에 문제가 생겨도, 문제가 생기지 않아도 Map 타입으로 감싸져 있기 때문에
        // 비어 있어도(유효성 검사 통과의 경우) 반환받는 컨트롤러에서 에러가 발생하지 않음.
        public Map<String, String> isValid( BindingResult result) {
                List<FieldError> fieldErrors = result.getFieldErrors();
                Map<String, String> response = new HashMap<>();

                // 1. 클라이언트가 전달한 필드의 값에(데이터가) 유효성 문제가 있다면
                // fieldErrors.size() != 0
                if(!fieldErrors.isEmpty()) {
                        // 에러가 발생한 필드 이름과 에러 메시지를 꺼내서 Map 에 담기.
                        fieldErrors.forEach(fieldError -> {
                                String fieldName = fieldError.getField();
                                String message = fieldError.getDefaultMessage();
                                response.put(fieldName, message);
                        });
                        return response;
                }
                return response;
        }
}
