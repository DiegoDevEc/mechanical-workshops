package com.mechanical.workshops.service;

import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.dto.ServiceSaveRequestDto;
import com.mechanical.workshops.dto.ServiceUpdateRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ServicesService {
    ResponseEntity<PageResponseDto> getAllServices(String text, int page, int size);
    ResponseEntity<ResponseDto> register(ServiceSaveRequestDto serviceSaveRequestDto);
    ResponseEntity<ResponseDto> update(UUID serviceId, ServiceUpdateRequestDto serviceUpdateRequestDto);
    ResponseEntity<ResponseDto> delete(UUID serviceId);
}
