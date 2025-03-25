package com.mechanical.workshops.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {

    @NotNull(message = "El correo electrónico es obligatorio")
    private String email;

    @NotNull(message = "La contraseña actual es obligatoria")
    private String newPassword;
}
