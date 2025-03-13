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
@RequestMapping("/users")
@AllArgsConstructor
@Tag(name = "Usuarios del sistema", description = "Controller para gestionar el usuarios del sistema (Operaciones CRUD)")
public class UsersController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<PageResponseDto> getAllUsersActives(@RequestParam(defaultValue = "") String text,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size) {
        return userService.getAllUserActive(Role.CLIENT, text, page, size);
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
