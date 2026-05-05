package com.example.backoffice.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backoffice.Entity.Ingredient;
import com.example.backoffice.Exception.notFoundException.IngredientNotFoundException;
import com.example.backoffice.Repository.IngredientRepository;

@Service
public class CategoryDeletionService {
    @Autowired
    private IngredientRepository ingredientRepository;
    private static final Logger logger = LoggerFactory.getLogger(IngredientServiceImp.class);

    public void setCategoryNull(String id) throws IngredientNotFoundException {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new IngredientNotFoundException(id));
        ingredient.setCategory(null);
        ingredientRepository.save(ingredient);
        logger.info("Set category to null for ingredient with name: {}", ingredient.getName());
    }
}
