package com.example.backoffice.DTO.dishRelation;

public class DishRelationUpdateDTO {
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
