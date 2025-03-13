package com.mechanical.workshops.service;

import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.ProductSaveRequestDto;
import com.mechanical.workshops.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ProductService {
    ResponseEntity<PageResponseDto> getAllProducts(String text, String categoryId, int page, int size);
    ResponseEntity<ResponseDto> register(ProductSaveRequestDto productSaveRequestDto);
    ResponseEntity<ResponseDto> update(UUID productId, ProductSaveRequestDto productSaveRequestDto);
    ResponseEntity<ResponseDto> delete(UUID productId);
}
