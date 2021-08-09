package com.example.bannerbidservice.service;

import com.example.bannerbidservice.controller.dto.AddCategoryDTO;
import com.example.bannerbidservice.controller.dto.CategoryResponseDTO;
import com.example.bannerbidservice.controller.dto.PatchCategoryDTO;
import com.example.bannerbidservice.entity.Category;
import java.util.List;

public interface CategoryService {

    boolean existsByName(String name);

    List<Category> getCategoriesByNameContainingIgnoreCase(String partName);

    List<Category> getActiveCategories();

    CategoryResponseDTO getCategoryResponseDTO(AddCategoryDTO addCategoryDTO);

    boolean existsById(Long id);

    boolean isNameCategoryNotUnique(Long id, String name);

    boolean isReqNameCategoryNotUnique(Long id, String name);

    CategoryResponseDTO getCategoryResponseDTO(PatchCategoryDTO patchCategoryDTO, Long id);
}