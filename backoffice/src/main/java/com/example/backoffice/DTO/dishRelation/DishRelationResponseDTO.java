package com.example.backoffice.DTO.dishRelation;

import com.example.backoffice.DTO.dishMetadata.DishMetadataResponseDTO;
import com.example.backoffice.DTO.ingredient.IngredientWithCategoryDTO;
import com.example.backoffice.DTO.label.LabelResponseDTO;

public class DishRelationResponseDTO {
    private String id;
    private DishMetadataResponseDTO dishMetadata;
    private IngredientWithCategoryDTO ingredient;
    private LabelResponseDTO label;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DishMetadataResponseDTO getDishMetadata() {
        return dishMetadata;
    }

    public void setDishMetadata(DishMetadataResponseDTO dishMetadata) {
        this.dishMetadata = dishMetadata;
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
