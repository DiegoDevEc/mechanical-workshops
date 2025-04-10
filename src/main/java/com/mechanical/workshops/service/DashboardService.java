package com.mechanical.workshops.service;

import com.mechanical.workshops.dto.AttendanceDashboardFilter;
import com.mechanical.workshops.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface DashboardService {
    ResponseEntity<ResponseDto> getDashboardAdministrator(AttendanceDashboardFilter filter);
    ResponseEntity<ResponseDto> getDashboardByClient(AttendanceDashboardFilter filter);
    ResponseEntity<ResponseDto> getDashboardByTechnician(AttendanceDashboardFilter filter);

}
