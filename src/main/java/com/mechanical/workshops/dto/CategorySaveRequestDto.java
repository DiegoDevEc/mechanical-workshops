package com.mechanical.workshops.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorySaveRequestDto {
    private String name;
    private String description;
}
