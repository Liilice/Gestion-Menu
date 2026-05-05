package com.example.backoffice.Mapper;

import org.mapstruct.*;

import com.example.backoffice.DTO.dishRelation.DishRelationResponseDTO;
import com.example.backoffice.Entity.DishRelation;

@Mapper(componentModel = "spring")
public interface DishRelationMapper {
    @Mapping(target = "dishMetadata", source = "dishMetadata")
    @Mapping(target = "ingredient", source = "ingredient")
    @Mapping(target = "label", source = "label")
    DishRelationResponseDTO toResponseDTO(DishRelation dishRelation);
}