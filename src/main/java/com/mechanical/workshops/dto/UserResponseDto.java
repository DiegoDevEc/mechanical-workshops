package com.mechanical.workshops.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private UUID id;
    private String firstname;
    private String lastname;
    private String address;
    private String username;
    private String identification;
    private String phone;
    private String email;
    private UUID personId;
}
