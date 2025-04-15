package com._7.bookinghospital.hospital_service.presentation.dto.request;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Getter
public class UpdateHospitalRequestDto {
    private String address;
    private String phone;
    private String description;
    private LocalTime openHour;
    private LocalTime closeHour;

    public Map<String, Object> extractUpdateFields() {
        Map<String, Object> fields = new HashMap<>();

        if(StringUtils.isNotBlank(address)) {
            fields.put("address", (String) address);
        }

        if(StringUtils.isNotBlank(phone)) {
            fields.put("phone", (String) phone);
        }

        if(StringUtils.isNotBlank(description)) {
            fields.put("description", (String) description);
        }

        if (openHour != null) {
            fields.put("openHour", (LocalTime) openHour);
        }

        if (closeHour != null) {
            fields.put("closeHour", (LocalTime) closeHour);
        }
        return fields;
    }
}
