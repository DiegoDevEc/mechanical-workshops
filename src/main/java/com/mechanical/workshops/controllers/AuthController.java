package com.mechanical.workshops.controllers;

import com.mechanical.workshops.dto.AuthRequestDto;
import com.mechanical.workshops.dto.AuthResponseDto;
import com.mechanical.workshops.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> authenticate(
            @RequestBody AuthRequestDto request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }
}