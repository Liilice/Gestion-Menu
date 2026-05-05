package com.example.backoffice.DTO.dishRelation;

import com.example.backoffice.DTO.ingredient.IngredientWithCategoryDTO;
import com.example.backoffice.DTO.label.LabelResponseDTO;

public class DishRelationWithoutDishMetadataResponseDTO {
    private String id;
    private IngredientWithCategoryDTO ingredient;
    private LabelResponseDTO label;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IngredientWithCategoryDTO getIngredient() {
        return ingredient;
    }

    public void setIngredient(IngredientWithCategoryDTO ingredient) {
        this.ingredient = ingredient;
    }

    public LabelResponseDTO getLabel() {
        return label;
    }

    public void setLabel(LabelResponseDTO label) {
        this.label = label;
    }
}
