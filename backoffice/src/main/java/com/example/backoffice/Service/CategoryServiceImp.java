package com.example.backoffice.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backoffice.DTO.category.CategoryDTO;
import com.example.backoffice.DTO.category.CategoryResponseDTO;
import com.example.backoffice.DTO.ingredient.IngredientSummaryDTO;
import com.example.backoffice.Entity.Category;
import com.example.backoffice.Exception.notFoundException.CategoryNotFoundException;
import com.example.backoffice.Mapper.CategoryMapper;
import com.example.backoffice.Repository.CategoryRepository;

@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryDeletionService categoryDeletionService;
    @Autowired
    private CategoryMapper categoryMapper;

    public List<CategoryResponseDTO> getAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponseDTO)
                .toList();
    }

    public CategoryResponseDTO create(CategoryDTO payload) {
        Category category = new Category();
        category.setName(payload.getName().toLowerCase());
        Category entity = categoryRepository.save(category);
        return categoryMapper.toResponseDTO(entity);
    }

    public CategoryResponseDTO getById(String id) throws CategoryNotFoundException {
        Category category = getEntityById(id);
        return categoryMapper.toResponseDTO(category);
    }

    @Transactional
    public CategoryResponseDTO updateById(String id, CategoryDTO payload) throws CategoryNotFoundException {
        Category category = getEntityById(id);
        category.setName(payload.getName().toLowerCase());
        return categoryMapper.toResponseDTO(categoryRepository.save(category));
    }

    @Transactional
    public void deleteById(String id) throws CategoryNotFoundException {
        Category category = getEntityById(id);
        CategoryResponseDTO categoryResponseDTO = categoryMapper.toResponseDTO(category);
        List<IngredientSummaryDTO> ingredientSummaryDTO = categoryResponseDTO.getIngredients();
        ingredientSummaryDTO.stream()
                .forEach((ingredient) -> categoryDeletionService.setCategoryNull(ingredient.getId()));
        categoryRepository.delete(category);
    }

    @Transactional
    public void deleteManyById(List<String> idsList) throws CategoryNotFoundException {
        idsList.stream().forEach((id) -> deleteById(id));
    }

    public Category getEntityById(String id) throws CategoryNotFoundException {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }
}