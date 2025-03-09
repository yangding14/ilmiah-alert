package com.ilmiah.ilmiah_alert.external.ilmiah.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProjectData(
        int id,
        String title,
        int session,
        int semester,
        String no_student,
        String supervisor,
        int department_id,
        String status) {}
