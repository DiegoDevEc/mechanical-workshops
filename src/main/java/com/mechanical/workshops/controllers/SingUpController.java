package com.mechanical.workshops.controllers;

import com.mechanical.workshops.dto.UserSaveRequestDTO;
import com.mechanical.workshops.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/public/users")
@AllArgsConstructor
public class SingUpController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserSaveRequestDTO userSaveRequestDTO) {
        return userService.register(userSaveRequestDTO);
    }

    @GetMapping("/validate/phone")
    public ResponseEntity<String> validatePhone(@RequestParam(required = false) UUID userId, @RequestParam String phone) {
        return userService.validatePhone(userId, phone);
    }

    @GetMapping("/validate/email")
    public ResponseEntity<String> validateEmail(@RequestParam(required = false) UUID userId, @RequestParam String email) {
        return userService.validateEmail(userId, email);
    }

    @GetMapping("/validate/username")
    public ResponseEntity<String> validateUsername(@RequestParam(required = false) UUID userId, @RequestParam String username) {
        return userService.validateUsername(userId, username);
    }

    @GetMapping("/validate/identification")
    public ResponseEntity<String> validateIdentification(@RequestParam(required = false) UUID userId, @RequestParam String identification) {
        return userService.validateIdentification(userId, identification);
    }

    @GetMapping("/validate/value-identification")
    public ResponseEntity<String> validateValueIdentification(@RequestParam String value) {
        return userService.validateValueIdentification(value);
    }
}
