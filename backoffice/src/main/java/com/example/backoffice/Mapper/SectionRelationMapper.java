package com.example.backoffice.Mapper;

import org.mapstruct.*;

import com.example.backoffice.DTO.sectionRelation.SectionRelationResponseDTO;
import com.example.backoffice.Entity.SectionRelation;

@Mapper(componentModel = "spring")
public interface SectionRelationMapper {
    @Mapping(target = "sectionMetadata", source = "sectionMetadata")
    @Mapping(target = "dishMetadata", source = "dishMetadata")
    SectionRelationResponseDTO toResponseDTO(SectionRelation sectionRelation);
}
