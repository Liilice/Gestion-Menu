package com.example.backoffice.DTO.ingredient;

import com.example.backoffice.DTO.category.CategoryWithoutIngredientDTO;

public class IngredientWithCategoryDTO {
    private String id;
    private String name;
    private CategoryWithoutIngredientDTO category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryWithoutIngredientDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryWithoutIngredientDTO category) {
        this.category = category;
    }
}