package com.example.backoffice.DTO.sectionRelation;

import com.example.backoffice.DTO.dishMetadata.DishMetadataForGetResponseDTO;
import com.example.backoffice.DTO.sectionMetadata.SectionMetadataResponseDTO;

public class SectionRelationResponseDTO {
    private String id;
    private SectionMetadataResponseDTO sectionMetadata;
    private DishMetadataForGetResponseDTO dishMetadata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SectionMetadataResponseDTO getSectionMetadata() {
        return sectionMetadata;
    }

    public void setSectionMetadata(SectionMetadataResponseDTO sectionMetadata) {
        this.sectionMetadata = sectionMetadata;
    }

    public DishMetadataForGetResponseDTO getDishMetadata() {
        return dishMetadata;
    }

    public void setDishMetadata(DishMetadataForGetResponseDTO dishMetadata) {
        this.dishMetadata = dishMetadata;
    }
}