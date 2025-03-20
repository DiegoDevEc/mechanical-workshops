package com.mechanical.workshops.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceResponseDto {

    private UUID appointmentId;

    private ServiceResponseDto service;

    private LocalDate startDate;

    private LocalDate endDate;

    private String comments;

    private String status;
}
