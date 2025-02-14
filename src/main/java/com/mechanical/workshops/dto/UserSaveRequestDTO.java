package com.mechanical.workshops.dto;

import com.mechanical.workshops.enums.Role;
import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSaveRequestDTO {
    // Datos de Persona
    @NotBlank(message = "El nombre es obligatorio")
    private String firstname;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastname;

    private String address;

    // Datos de Usuario
    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "La identificación es obligatoria")
    @Pattern(regexp = "\\d{10,13}", message = "La identificación debe tener entre 10 y 13 dígitos")
    private String identification;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{7,15}", message = "El teléfono debe tener entre 7 y 15 dígitos")
    private String phone;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Debe ser un correo válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @NotNull(message = "El rol es obligatorio")
    private Role role;
}
