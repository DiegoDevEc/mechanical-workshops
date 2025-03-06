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
public class ServiceSaveRequestDto {

    @NotNull(message = "La descripcion es obligatoria")
    private String description;
    @NotNull(message = "El nombre es obligatorio")
    private String name;
    @NotNull(message = "El costo es obligatorio")
    private BigDecimal cost;
}
