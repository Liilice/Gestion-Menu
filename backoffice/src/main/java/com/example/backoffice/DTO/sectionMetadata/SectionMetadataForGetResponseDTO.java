package com.example.backoffice.DTO.sectionMetadata;

import java.util.List;

import com.example.backoffice.DTO.sectionRelation.SectionRelationWithoutSectionMetadataDTO;

public class SectionMetadataForGetResponseDTO {
    private String id;
    private String name;
    private String description;
    private List<SectionRelationWithoutSectionMetadataDTO> sectionRelation;

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

    public List<SectionRelationWithoutSectionMetadataDTO> getSectionRelation() {
        return sectionRelation;
    }

    public void setSectionRelation(List<SectionRelationWithoutSectionMetadataDTO> sectionRelation) {
        this.sectionRelation = sectionRelation;
    }
}
