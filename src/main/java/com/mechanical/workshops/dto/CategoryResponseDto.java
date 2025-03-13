package com.mechanical.workshops.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {
    private UUID id;
    private String name;
    private String description;
}
