package com.example.backoffice.Service;

import java.util.List;

import com.example.backoffice.DTO.sectionMetadata.SectionMetadataDTO;
import com.example.backoffice.DTO.sectionMetadata.SectionMetadataForGetResponseDTO;
import com.example.backoffice.DTO.sectionMetadata.SectionMetadataResponseDTO;
import com.example.backoffice.Entity.SectionMetadata;
import com.example.backoffice.Exception.notFoundException.SectionMetadataNotFoundException;

public interface SectionMetadataService {
    List<SectionMetadataForGetResponseDTO> getAll();

    SectionMetadataResponseDTO create(SectionMetadataDTO payload);

    SectionMetadataForGetResponseDTO getById(String id) throws SectionMetadataNotFoundException;

    SectionMetadataResponseDTO updateById(String id, SectionMetadataDTO payload) throws SectionMetadataNotFoundException;

    void deleteById(String id) throws SectionMetadataNotFoundException;

    SectionMetadata getEntityById(String id);
}