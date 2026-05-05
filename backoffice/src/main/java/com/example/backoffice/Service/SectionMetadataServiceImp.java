package com.example.backoffice.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backoffice.DTO.sectionMetadata.SectionMetadataDTO;
import com.example.backoffice.DTO.sectionMetadata.SectionMetadataForGetResponseDTO;
import com.example.backoffice.DTO.sectionMetadata.SectionMetadataResponseDTO;
import com.example.backoffice.Entity.SectionMetadata;
import com.example.backoffice.Exception.notFoundException.SectionMetadataNotFoundException;
import com.example.backoffice.Mapper.SectionMetadataMapper;
import com.example.backoffice.Repository.SectionMetadataRepository;

@Service
public class SectionMetadataServiceImp implements SectionMetadataService {
    @Autowired
    private SectionMetadataRepository sectionMetadataRepository;
    @Autowired
    private SectionMetadataMapper sectionMetadataMapper;

    public List<SectionMetadataForGetResponseDTO> getAll() {
        return sectionMetadataRepository.findAll().stream().map(sectionMetadataMapper::toForGetResponseDTO).toList();
    }

    public SectionMetadataResponseDTO create(SectionMetadataDTO payload) {
        SectionMetadata sectionMetadata = new SectionMetadata();
        sectionMetadata.setName(payload.getName().toLowerCase());
        if (payload.getDescription() != null) {
            sectionMetadata.setDescription(payload.getDescription().toLowerCase());
        }
        return sectionMetadataMapper.toResponseDTO(sectionMetadataRepository.save(sectionMetadata));
    }

    public SectionMetadataForGetResponseDTO getById(String id) throws SectionMetadataNotFoundException {
        SectionMetadata sectionMetadata = getEntityById(id);
        return sectionMetadataMapper.toForGetResponseDTO(sectionMetadata);
    }

    public SectionMetadataResponseDTO updateById(String id, SectionMetadataDTO payload)
            throws SectionMetadataNotFoundException {
        SectionMetadata sectionMetadata = getEntityById(id);
        sectionMetadataMapper.updateFromDto(payload, sectionMetadata);
        return sectionMetadataMapper.toResponseDTO(sectionMetadataRepository.save(sectionMetadata));
    }

    public void deleteById(String id) throws SectionMetadataNotFoundException {
        SectionMetadata sectionMetadata = getEntityById(id);
        sectionMetadataRepository.delete(sectionMetadata);
    }

    public SectionMetadata getEntityById(String id) throws SectionMetadataNotFoundException {
        return sectionMetadataRepository.findById(id)
                .orElseThrow(() -> new SectionMetadataNotFoundException(id));
    }

}
