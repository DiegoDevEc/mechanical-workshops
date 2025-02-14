package com.mechanical.workshops.service;

import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.UserSaveRequestDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;


public interface UserService {
    String login(String username, String password);

    ResponseEntity<PageResponseDto> getAllUserActive(String text, int page, int size);
    ResponseEntity<String> delete(UUID userId);

    ResponseEntity<String> register(UserSaveRequestDTO userSaveRequestDTO);
    ResponseEntity<String> update(UUID userId, UserSaveRequestDTO userSaveRequestDTO);
    ResponseEntity<String> validateEmail(UUID userId, String email);
    ResponseEntity<String> validatePhone(UUID userId, String phone);
    ResponseEntity<String> validateUsername(UUID userId, String username);
    ResponseEntity<String> validateIdentification(UUID userId, String identification);
    ResponseEntity<String> validateValueIdentification(String value);
}
