package com.mechanical.workshops.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableAppointmentDaysDto {
    private String dayName;
    private int day;
    private String month;
    private String date;
    private boolean selected;
}
