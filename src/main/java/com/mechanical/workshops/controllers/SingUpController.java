package com.mechanical.workshops.controllers;

import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.dto.UserSaveRequestDTO;
import com.mechanical.workshops.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/public/users")
@AllArgsConstructor
@Tag(name = "Registrarse y Validar usuarios", description = "Controller para gestionar el registro de usuarios (Operaciones CRUD)")
public class SingUpController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerUser(@RequestBody @Valid UserSaveRequestDTO userSaveRequestDTO) {
        return userService.register(userSaveRequestDTO);
    }

    @GetMapping("/validate/phone")
    public ResponseEntity<ResponseDto> validatePhone(@RequestParam(required = false) UUID userId, @RequestParam String phone) {
        return userService.validatePhone(userId, phone);
    }

    @GetMapping("/validate/email")
    public ResponseEntity<ResponseDto> validateEmail(@RequestParam(required = false) UUID userId, @RequestParam String email) {
        return userService.validateEmail(userId, email);
    }

    @GetMapping("/validate/username")
    public ResponseEntity<ResponseDto> validateUsername(@RequestParam(required = false) UUID userId, @RequestParam String username) {
        return userService.validateUsername(userId, username);
    }

    @GetMapping("/validate/identification")
    public ResponseEntity<ResponseDto> validateIdentification(@RequestParam(required = false) UUID userId, @RequestParam String identification) {
        return userService.validateIdentification(userId, identification);
    }

    @GetMapping("/validate/value-identification")
    public ResponseEntity<ResponseDto> validateValueIdentification(@RequestParam String value) {
        return userService.validateValueIdentification(value);
    }
}
