package com.mechanical.workshops.dto;

import lombok.*;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentAssignedRequestDto {

    private UUID serviceId;

    private UUID technicalId;
}

