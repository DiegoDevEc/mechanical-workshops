package com.mechanical.workshops.controllers;

import com.mechanical.workshops.dto.AuthRequestDto;
import com.mechanical.workshops.dto.AuthResponseDto;
import com.mechanical.workshops.dto.ChangePasswordDto;
import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.security.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/auth")
@RequiredArgsConstructor
@Tag(name = "Autentificación", description = "Controller para autentificar usuarios (Operaciones LOGIN)")
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    @Operation(summary = "Login By username and password")
    public ResponseEntity<AuthResponseDto> authenticate(
            @RequestBody AuthRequestDto request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ResponseDto> changePassword(
            @RequestBody ChangePasswordDto changePasswordDto) {
        return service.changePassword(changePasswordDto);
    }
}