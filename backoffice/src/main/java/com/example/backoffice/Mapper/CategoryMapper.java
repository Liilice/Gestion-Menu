package com.example.backoffice.Mapper;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.backoffice.DTO.category.CategoryResponseDTO;
import com.example.backoffice.Entity.Category;

@Component
public class CategoryMapper {
  @Autowired IngredientMapper ingredientMapper;

  public CategoryResponseDTO toResponseDTO(Category category) {
    CategoryResponseDTO dto = new CategoryResponseDTO();
    dto.setId(category.getId());
    dto.setName(category.getName());

    if (category.getIngredients() == null) {
      dto.setIngredients(Collections.emptyList());
      return dto;
    }

    dto.setIngredients(category.getIngredients().stream()
        .map(ingredientMapper::toIngredientSummaryDTO)
        .toList());
    return dto;
  }
}
