package com.example.backoffice.DTO.category;

import java.util.List;

import com.example.backoffice.DTO.ingredient.IngredientSummaryDTO;

public class CategoryResponseDTO {
    private String id;
    private String name;
    private List<IngredientSummaryDTO> ingredients;

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

    public List<IngredientSummaryDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientSummaryDTO> ingredients) {
        this.ingredients = ingredients;
    }
}