package com.example.backoffice.DTO.ingredient;

import jakarta.validation.constraints.NotBlank;

public class IngredientDTO {
    @NotBlank(message = "Name is required")
    private String name;

    private String categoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

}