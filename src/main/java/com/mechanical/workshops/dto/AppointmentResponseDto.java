package com.mechanical.workshops.dto;

import com.mechanical.workshops.models.AvailableAppointment;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponseDto {

    private UUID id;

    private UserResponseDto client;

    private VehicleResponseDto vehicle;

    private LocalDate dateAppointment;

    private String status;

    private AvailableAppointmentDto availableAppointment;

    private String statusAttendance;

    private String code;

    private UserResponseDto technician;

    private ServiceResponseDto service;
}

