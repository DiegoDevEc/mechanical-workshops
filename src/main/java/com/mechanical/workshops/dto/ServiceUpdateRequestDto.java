package com.mechanical.workshops.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceUpdateRequestDto {

    @NotNull(message = "El nombre es obligatorio")
    private String description;
    @NotNull(message = "El nombre es obligatorio")
    private String name;
    @NotNull(message = "El costo es obligatorio")
    @Min(value = 0, message = "El costo debe ser mayor o igual a 0")
    private BigDecimal cost;
}
