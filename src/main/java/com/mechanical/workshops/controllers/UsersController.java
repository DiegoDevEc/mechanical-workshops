package com.mechanical.workshops.controllers;

import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.UserSaveRequestDTO;
import com.mechanical.workshops.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUser(@RequestBody UserSaveRequestDTO userSaveRequestDTO, @PathVariable UUID userId) {
        return userService.update(userId, userSaveRequestDTO);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID userId) {
        return userService.delete(userId);
    }

}
