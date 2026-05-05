package com.example.backoffice.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backoffice.DTO.sectionRelation.SectionRelationDTO;
import com.example.backoffice.DTO.sectionRelation.SectionRelationResponseDTO;
import com.example.backoffice.Entity.DishMetadata;
import com.example.backoffice.Entity.SectionMetadata;
import com.example.backoffice.Entity.SectionRelation;
import com.example.backoffice.Exception.notFoundException.SectionRelationNotFoundException;
import com.example.backoffice.Mapper.SectionRelationMapper;
import com.example.backoffice.Repository.SectionRelationRepository;

@Service
public class SectionRelationServiceImp implements SectionRelationService {
    @Autowired
    private SectionRelationRepository sectionRelationRepository;
    @Autowired
    private SectionRelationMapper sectionRelationMapper;
    @Autowired
    private SectionMetadataService sectionMetadataService;
    @Autowired
    private DishMetadataService dishMetadataService;

    public List<SectionRelationResponseDTO> getAll() {
        return sectionRelationRepository.findAll().stream().map(sectionRelationMapper::toResponseDTO).toList();
    }

    public SectionRelationResponseDTO create(SectionRelationDTO payload) {
        SectionMetadata menuMetadata = sectionMetadataService.getEntityById(payload.getSectionMetadataId());
        DishMetadata dishMetadata = dishMetadataService.getEntityById(payload.getDishMetadataId());
        SectionRelation menuRelation = new SectionRelation();
        menuRelation.setSectionMetadata(menuMetadata);
        menuRelation.setDishMetadata(dishMetadata);
        return sectionRelationMapper.toResponseDTO(sectionRelationRepository.save(menuRelation));
    }

    public SectionRelationResponseDTO getById(String id) {
        SectionRelation menuRelation = getEntityById(id);
        return sectionRelationMapper.toResponseDTO(menuRelation);
    }

    public SectionRelationResponseDTO updateById(String id, SectionRelationDTO payload) throws SectionRelationNotFoundException {
        SectionRelation menuRelation = getEntityById(id);

        if (payload.getSectionMetadataId() != null) {
            SectionMetadata menuMetadata = sectionMetadataService.getEntityById(payload.getSectionMetadataId());
            menuRelation.setSectionMetadata(menuMetadata);
        }

        if (payload.getDishMetadataId() != null) {
            DishMetadata dishMetadata = dishMetadataService.getEntityById(payload.getDishMetadataId());
            menuRelation.setDishMetadata(dishMetadata);
        }

        return sectionRelationMapper.toResponseDTO(sectionRelationRepository.save(menuRelation));
    }

    public void deleteById(String id) throws SectionRelationNotFoundException {
        SectionRelation menuRelation = getEntityById(id);
        sectionRelationRepository.delete(menuRelation);
    }

    public SectionRelation getEntityById(String id) throws SectionRelationNotFoundException {
        return sectionRelationRepository.findById(id)
                .orElseThrow(() -> new SectionRelationNotFoundException(id));
    }

}
