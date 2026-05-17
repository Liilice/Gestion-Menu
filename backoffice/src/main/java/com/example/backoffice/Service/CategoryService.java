package com.example.backoffice.Service;

import java.util.List;

import com.example.backoffice.DTO.category.CategoryDTO;
import com.example.backoffice.DTO.category.CategoryResponseDTO;
import com.example.backoffice.Entity.Category;
import com.example.backoffice.Exception.notFoundException.CategoryNotFoundException;

public interface CategoryService {
    List<CategoryResponseDTO> getAll();

    CategoryResponseDTO create(CategoryDTO payload);

    CategoryResponseDTO getById(String id) throws CategoryNotFoundException;

    CategoryResponseDTO updateById(String id, CategoryDTO payload) throws CategoryNotFoundException;

    void deleteById(String id) throws CategoryNotFoundException;

    void deleteManyById(List<String> idsList) throws CategoryNotFoundException;

    Category getEntityById(String id) throws CategoryNotFoundException;
}