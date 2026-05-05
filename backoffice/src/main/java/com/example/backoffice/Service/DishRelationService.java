package com.example.backoffice.Service;

import java.util.List;

import com.example.backoffice.DTO.dishRelation.DishRelationDTO;
import com.example.backoffice.DTO.dishRelation.DishRelationResponseDTO;
import com.example.backoffice.DTO.dishRelation.DishRelationUpdateDTO;
import com.example.backoffice.Entity.DishRelation;
import com.example.backoffice.Exception.notFoundException.DishRelationNotFoundException;

public interface DishRelationService {
    List<DishRelationResponseDTO> getAll();

    DishRelationResponseDTO create(DishRelationDTO dishRelationDTO);

    DishRelationResponseDTO getById(String id) throws DishRelationNotFoundException;

    DishRelationResponseDTO updateById(String id, DishRelationUpdateDTO dishRelationDTO) throws DishRelationNotFoundException;

    void deleteById(String id) throws DishRelationNotFoundException;

    DishRelation getEntityById(String id) throws DishRelationNotFoundException;

}
