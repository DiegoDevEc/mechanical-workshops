package com.mechanical.workshops.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableAppointmentDto {

    private UUID id;
    private LocalDate dateAvailable;
    private LocalTime timeAvailable;
    private String dayOfWeek;
    private String code;
}
