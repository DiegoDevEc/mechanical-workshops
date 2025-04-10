package com.mechanical.workshops.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceDashboardResponse {
    private Map<String, Long> dataStatus;
    private Map<String, BigDecimal> dataValueMount;
    private Map<String, BigDecimal> finishValueMount;
}
