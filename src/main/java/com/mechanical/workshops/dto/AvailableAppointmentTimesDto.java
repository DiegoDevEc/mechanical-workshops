package com.mechanical.workshops.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableAppointmentTimesDto {
    private UUID id;
    private String start;
    private boolean selected;
    private String code;
}
