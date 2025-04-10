package com.mechanical.workshops.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceDashboardFilter {
    private UUID entityId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String year;
}
