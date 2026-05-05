package com.example.backoffice.Service;

import java.util.List;

import com.example.backoffice.DTO.sectionRelation.SectionRelationDTO;
import com.example.backoffice.DTO.sectionRelation.SectionRelationResponseDTO;
import com.example.backoffice.Entity.SectionRelation;
import com.example.backoffice.Exception.notFoundException.SectionRelationNotFoundException;

public interface SectionRelationService {
    List<SectionRelationResponseDTO> getAll();

    SectionRelationResponseDTO create(SectionRelationDTO payload);

    SectionRelationResponseDTO getById(String id) throws SectionRelationNotFoundException;

    SectionRelationResponseDTO updateById(String id, SectionRelationDTO payload) throws SectionRelationNotFoundException;

    void deleteById(String id) throws SectionRelationNotFoundException;

    SectionRelation getEntityById(String id) throws SectionRelationNotFoundException;
}