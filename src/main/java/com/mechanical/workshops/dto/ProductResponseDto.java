package com.mechanical.workshops.dto;

import com.mechanical.workshops.enums.Status;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private UUID id;
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private Status status;
    private CategoryResponseDto category;
}
