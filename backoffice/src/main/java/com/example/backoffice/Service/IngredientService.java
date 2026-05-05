package com.example.backoffice.Service;

import java.util.List;

import com.example.backoffice.DTO.ingredient.IngredientDTO;
import com.example.backoffice.DTO.ingredient.IngredientResponseDTO;
import com.example.backoffice.DTO.ingredient.IngredientUpdateDTO;
import com.example.backoffice.Exception.notFoundException.CategoryNotFoundException;
import com.example.backoffice.Exception.notFoundException.IngredientNotFoundException;
import com.example.backoffice.Entity.Ingredient;

public interface IngredientService {
    List<IngredientResponseDTO> getAll();
    IngredientResponseDTO create(IngredientDTO ingredientDTO) throws CategoryNotFoundException;
    IngredientResponseDTO getById(String id) throws IngredientNotFoundException;
    IngredientResponseDTO updateById(String id, IngredientUpdateDTO ingredientUpdateDTO) throws IngredientNotFoundException, CategoryNotFoundException;
    void deleteById(String id) throws IngredientNotFoundException;
    Ingredient getEntityById(String id);
} 