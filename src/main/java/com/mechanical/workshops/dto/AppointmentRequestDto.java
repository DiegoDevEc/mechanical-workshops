package com.mechanical.workshops.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentRequestDto {

    @NotNull(message = "El cliente es obligatorio")
    private UUID clientId;

    @NotNull(message = "El vehículo es obligatorio")
    private UUID vehicleId;

    @NotNull(message = "La cita disponible es obligatoria")
    private UUID availableAppointmentId;

    @NotNull(message = "El servicio es obligatorio")
    private UUID serviceId;
}

