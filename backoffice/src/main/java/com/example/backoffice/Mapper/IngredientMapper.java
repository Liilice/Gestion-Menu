package com.example.backoffice.Mapper;

import org.mapstruct.*;

import com.example.backoffice.DTO.ingredient.IngredientResponseDTO;
import com.example.backoffice.DTO.ingredient.IngredientSummaryDTO;
import com.example.backoffice.DTO.ingredient.IngredientUpdateDTO;
import com.example.backoffice.Entity.Ingredient;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    // Entity → Response DTO
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    IngredientResponseDTO toResponseDTO(Ingredient ingredient);

    // Entity → Summary
    IngredientSummaryDTO toIngredientSummaryDTO(Ingredient ingredient);

    // Partial update — "category" is ignored here because the DTO only carries a categoryId (String),
    // which cannot be automatically resolved to a Category entity by MapStruct.
    // The caller (IngredientServiceImp) is responsible for fetching the Category and setting it manually.
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "dishRelations", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromDto(IngredientUpdateDTO dto, @MappingTarget Ingredient entity);
}