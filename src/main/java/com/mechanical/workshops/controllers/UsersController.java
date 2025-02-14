package com.mechanical.workshops.controllers;

import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.UserResponseDto;
import com.mechanical.workshops.dto.UserSaveRequestDTO;
import com.mechanical.workshops.models.User;
import com.mechanical.workshops.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UsersController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<PageResponseDto> getAllUsersActives(@RequestParam(defaultValue = "") String text,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "5") int size) {
        return userService.getAllUserActive( text, page, size);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserSaveRequestDTO userSaveRequestDTO) {
        return userService.register(userSaveRequestDTO);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUser(@RequestBody UserSaveRequestDTO userSaveRequestDTO, @PathVariable UUID userId) {
        return userService.update(userId, userSaveRequestDTO);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID userId) {
        return userService.delete(userId);
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
