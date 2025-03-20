package com.mechanical.workshops.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleSaveRequestDto {

    private UUID clientId;

    private String brand;

    private String model;

    private int year;

    private String plate;
}
