package com.mechanical.workshops.service;

import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.dto.UserSaveRequestDTO;
import com.mechanical.workshops.enums.Role;
import org.springframework.http.ResponseEntity;

import java.util.UUID;


public interface UserService {
    ResponseEntity<PageResponseDto> getAllUserActive(Role role, String text, int page, int size);
    ResponseEntity<ResponseDto> delete(UUID userId);

    ResponseEntity<ResponseDto> register(UserSaveRequestDTO userSaveRequestDTO);
    ResponseEntity<ResponseDto> update(UUID userId, UserSaveRequestDTO userSaveRequestDTO);
    ResponseEntity<ResponseDto> validateEmail(UUID userId, String email);
    ResponseEntity<ResponseDto> validatePhone(UUID userId, String phone);
    ResponseEntity<ResponseDto> validateUsername(UUID userId, String username);
    ResponseEntity<ResponseDto> validateIdentification(UUID userId, String identification);
    ResponseEntity<ResponseDto> validateValueIdentification(String value);
}
