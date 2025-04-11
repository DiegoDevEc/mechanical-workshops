package com.mechanical.workshops.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttendanceRequestDto {

    private UUID appointmentId;

    private UUID serviceId;

    private LocalDate startDate;

    private LocalDate endDate;

    private String comments;

    private String status;
}
