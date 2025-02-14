package com.mechanical.workshops.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private String firstname;
    private String lastname;
    private String address;
    private String username;
    private String identification;
    private String phone;
    private String email;
}
