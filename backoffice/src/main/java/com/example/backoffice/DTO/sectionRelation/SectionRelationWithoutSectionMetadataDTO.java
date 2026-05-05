package com.example.backoffice.DTO.sectionRelation;

import com.example.backoffice.DTO.dishMetadata.DishMetadataForGetResponseDTO;

public class SectionRelationWithoutSectionMetadataDTO {
    private String id;
    private DishMetadataForGetResponseDTO dishMetadata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DishMetadataForGetResponseDTO getDishMetadata() {
        return dishMetadata;
    }

    public void setDishMetadata(DishMetadataForGetResponseDTO dishMetadata) {
        this.dishMetadata = dishMetadata;
    }
}
