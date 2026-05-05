package com.example.backoffice.Service;

import java.util.List;

import com.example.backoffice.DTO.dishMetadata.DishMetadataDTO;
import com.example.backoffice.DTO.dishMetadata.DishMetadataForGetResponseDTO;
import com.example.backoffice.DTO.dishMetadata.DishMetadataResponseDTO;
import com.example.backoffice.DTO.dishMetadata.DishMetadataUpdateDTO;
import com.example.backoffice.Entity.DishMetadata;
import com.example.backoffice.Exception.notFoundException.DishMetadataNotFoundException;

public interface DishMetadataService {
    List<DishMetadataForGetResponseDTO> getAll();
    DishMetadataResponseDTO create(DishMetadataDTO payload);
    DishMetadataForGetResponseDTO getById(String id) throws DishMetadataNotFoundException;
    DishMetadataResponseDTO updateById(String id, DishMetadataUpdateDTO payload) throws DishMetadataNotFoundException;
    void deleteById(String id) throws DishMetadataNotFoundException;
    DishMetadata getEntityById(String id);
} 