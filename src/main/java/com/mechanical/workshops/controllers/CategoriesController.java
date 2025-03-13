package com.mechanical.workshops.controllers;

import com.mechanical.workshops.dto.CategorySaveRequestDto;
import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/category")
@AllArgsConstructor
@Tag(name = "Categorias de productos", description = "Controller para categorias de productos (Operaciones CRUD)")
public class CategoriesController {

    private final CategoryService categoryService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerCategory(@RequestBody CategorySaveRequestDto categorySaveRequestDto) {
        return categoryService.register(categorySaveRequestDto);
    }

    @GetMapping("/all")
    public ResponseEntity<PageResponseDto> getAllCategories(@RequestParam(defaultValue = "") String text,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "5") int size) {
        return categoryService.getAllCategories(text, page, size);
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<ResponseDto> updateUser(@RequestBody CategorySaveRequestDto categorySaveRequestDto, @PathVariable UUID categoryId) {
        return categoryService.update(categoryId, categorySaveRequestDto);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ResponseDto> deleteUser(@PathVariable UUID categoryId) {
        return categoryService.delete(categoryId);
    }
}
