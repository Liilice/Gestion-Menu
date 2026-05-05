package com.example.backoffice.Mapper;

import org.mapstruct.Mapper;

import com.example.backoffice.DTO.label.LabelResponseDTO;
import com.example.backoffice.Entity.Label;

@Mapper(componentModel = "spring")
public interface LabelMapper {
    LabelResponseDTO toResponseDTO(Label label);
    
}