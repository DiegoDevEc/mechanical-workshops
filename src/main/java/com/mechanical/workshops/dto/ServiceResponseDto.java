package com.mechanical.workshops.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponseDto {
    private UUID id;
    private String description;
    private String name;
    private BigDecimal cost;
}
