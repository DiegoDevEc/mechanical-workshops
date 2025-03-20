package com.mechanical.workshops.dto;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceRequestDto {

    private UUID appointmentId;

    private UUID serviceId;

    private LocalDate startDate;

    private LocalDate endDate;

    private String comments;

    private String status;
}
