package com.mechanical.workshops.controllers;

import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.dto.UserSaveRequestDTO;
import com.mechanical.workshops.enums.Role;
import com.mechanical.workshops.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/technician")
@AllArgsConstructor
@Tag(name = "Técnicos", description = "Controller para gestionar el técnicos de la empresa (Operaciones CRUD)")
public class TechnicianController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerService(@Valid @RequestBody UserSaveRequestDTO userSaveRequestDTO){
        return userService.register(userSaveRequestDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<PageResponseDto> getAllUserActive(@RequestParam(required = false) String text, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return userService.getAllUserActive(Role.TECHNICIAN ,text, page, size);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ResponseDto> updateUser(@RequestBody UserSaveRequestDTO userSaveRequestDTO, @PathVariable UUID userId) {
        return userService.update(userId, userSaveRequestDTO);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ResponseDto> deleteUser(@PathVariable UUID userId) {
        return userService.delete(userId);
    }

}
