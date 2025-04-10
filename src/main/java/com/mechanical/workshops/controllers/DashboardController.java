package com.mechanical.workshops.controllers;

import com.mechanical.workshops.dto.AttendanceDashboardFilter;
import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@AllArgsConstructor
@Tag(name = "Dashboard para roles", description = "Controller gestiona los endpoints de Dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @PostMapping("/administrator")
    public ResponseEntity<ResponseDto> getDashboardAdministrator(@RequestBody AttendanceDashboardFilter filter) {
        return dashboardService.getDashboardAdministrator(filter);
    }
    @PostMapping("/client")
    public ResponseEntity<ResponseDto> getDashboardClient(@RequestBody AttendanceDashboardFilter filter) {
        return dashboardService.getDashboardByClient(filter);
    }


    @PostMapping("/technician")
    public ResponseEntity<ResponseDto> getDashboardTechnician(@RequestBody AttendanceDashboardFilter filter) {
        return dashboardService.getDashboardByTechnician(filter);
    }

}
