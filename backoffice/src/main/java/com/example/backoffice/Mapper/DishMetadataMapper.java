package com.example.backoffice.Mapper;

import org.mapstruct.*;

import com.example.backoffice.DTO.dishMetadata.DishMetadataForGetResponseDTO;
import com.example.backoffice.DTO.dishMetadata.DishMetadataResponseDTO;
import com.example.backoffice.DTO.dishMetadata.DishMetadataUpdateDTO;
import com.example.backoffice.Entity.DishMetadata;

@Mapper(componentModel = "spring")
public interface DishMetadataMapper {
    DishMetadataResponseDTO toResponseDTO(DishMetadata dishMetadata);

    @Mapping(target = "dishRelation", source = "dishRelation")
    DishMetadataForGetResponseDTO toForGetResponseDTO(DishMetadata dishMetadata);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "dishRelation", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromDto(DishMetadataUpdateDTO dto, @MappingTarget DishMetadata entity);

}