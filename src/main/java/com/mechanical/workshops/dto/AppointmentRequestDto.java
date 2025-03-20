package com.mechanical.workshops.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentRequestDto {

    private UUID clientId;

    private UUID vehicleId;

    private LocalDate dateAppointment;

    private String status;

    private UUID availableAppointmentId;
}

