package com.mechanical.workshops.service;

import com.mechanical.workshops.dto.CategorySaveRequestDto;
import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface CategoryService {
    ResponseEntity<PageResponseDto> getAllCategories(String text, int page, int size);
    ResponseEntity<ResponseDto> register(CategorySaveRequestDto categorySaveRequestDto);
    ResponseEntity<ResponseDto> update(UUID categoryId, CategorySaveRequestDto categorySaveRequestDto);
    ResponseEntity<ResponseDto> delete(UUID categoryId);
}
