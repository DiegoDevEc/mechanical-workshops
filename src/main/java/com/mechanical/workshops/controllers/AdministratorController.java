package com.mechanical.workshops.controllers;

import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.dto.UserSaveRequestDTO;
import com.mechanical.workshops.enums.Role;
import com.mechanical.workshops.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/administrator")
@AllArgsConstructor
@Tag(name = "Administrator", description = "Administrator services")
public class AdministratorController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerUser(@RequestBody UserSaveRequestDTO userSaveRequestDTO) {
        return userService.register(userSaveRequestDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<PageResponseDto> getAllUsersActives(@RequestParam(defaultValue = "") String text,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "5") int size) {
        return userService.getAllUserActive(Role.ADMINISTRATOR, text, page, size);
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
