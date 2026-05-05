package com.example.backoffice.DTO.dishRelation;

import jakarta.validation.constraints.NotBlank;

public class DishRelationDTO {
    @NotBlank(message = "DishMetadataId is required")
    private String dishMetadataId;
    private String ingredientId;
    private String labelId;

    public String getDishMetadataId() {
        return dishMetadataId;
    }

    public void setDishMetadataId(String dishMetadataId) {
        this.dishMetadataId = dishMetadataId;
    }

    public String getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(String ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }
}
