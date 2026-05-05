package com.example.backoffice.DTO.sectionRelation;

public class SectionRelationDTO {
    private String sectionMetadataId;
    private String dishMetadataId;

    public String getSectionMetadataId() {
        return sectionMetadataId;
    }

    public String getDishMetadataId() {
        return dishMetadataId;
    }

    public void setSectionMetadataId(String sectionMetadataId) {
        this.sectionMetadataId = sectionMetadataId;
    }

    public void setDishMetadataId(String dishMetadataId) {
        this.dishMetadataId = dishMetadataId;
    }
}
