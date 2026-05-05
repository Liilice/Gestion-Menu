package com.example.backoffice.DTO.dishMetadata;

import java.math.BigDecimal;
import java.util.List;

import com.example.backoffice.DTO.dishRelation.DishRelationWithoutDishMetadataResponseDTO;

public class DishMetadataForGetResponseDTO {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private List<DishRelationWithoutDishMetadataResponseDTO> dishRelation;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<DishRelationWithoutDishMetadataResponseDTO> getDishRelation() {
        return dishRelation;
    }

    public void setDishRelation(List<DishRelationWithoutDishMetadataResponseDTO> dishRelation) {
        this.dishRelation = dishRelation;
    }
}
