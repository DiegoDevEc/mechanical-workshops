package com.mechanical.workshops.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleSaveRequestDto {

    @NotNull(message = "El cliente es obligatorio")
    private UUID clientId;

    @NotNull(message = "La marca es obligatoria")
    private String brand;

    @NotNull(message = "El modelo es obligatorio")
    private String model;

    @NotNull(message = "El año es obligatorio")
    @Min(value = 1900, message = "El año debe ser mayor o igual a 1900")
    private int year;

    @NotNull(message = "La placa es obligatoria")
    private String plate;
}
