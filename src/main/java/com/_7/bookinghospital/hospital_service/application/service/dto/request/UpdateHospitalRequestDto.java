package com._7.bookinghospital.hospital_service.application.service.dto.request;

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

    public <T> Map<String, T> extractUpdateFields() {
        Map<String, T> fields = new HashMap<>();

        // if(this.address != null && !this.address.isBlank()) {
        if(StringUtils.isNotBlank(address)) {
            fields.put("address", (T) address);
        }

         // if (phone != null && !phone.isBlank()) {
        if(StringUtils.isNotBlank(phone)) {
            fields.put("phone", (T) phone);
        }

         // if (description != null && !description.isBlank()) {
        if(StringUtils.isNotBlank(description)) {
            fields.put("description", (T) description);
        }

        if (openHour != null) {
            fields.put("openHour", (T) openHour);
        }

        if (closeHour != null) {
            fields.put("closeHour", (T) closeHour);
        }

        return fields;
    }
}
