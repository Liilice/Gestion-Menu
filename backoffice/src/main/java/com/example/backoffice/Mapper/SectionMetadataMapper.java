package com.example.backoffice.Mapper;

import org.mapstruct.*;

import com.example.backoffice.DTO.sectionMetadata.SectionMetadataDTO;
import com.example.backoffice.DTO.sectionMetadata.SectionMetadataForGetResponseDTO;
import com.example.backoffice.DTO.sectionMetadata.SectionMetadataResponseDTO;
import com.example.backoffice.Entity.SectionMetadata;

@Mapper(componentModel = "spring")
public interface SectionMetadataMapper {
    SectionMetadataResponseDTO toResponseDTO(SectionMetadata sectionMetadata);

    SectionMetadataForGetResponseDTO toForGetResponseDTO(SectionMetadata sectionMetadata);

    // Update partiel
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "sectionRelation", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromDto(SectionMetadataDTO dto, @MappingTarget SectionMetadata entity);
}