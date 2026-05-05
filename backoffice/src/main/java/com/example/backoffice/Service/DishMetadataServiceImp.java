package com.example.backoffice.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backoffice.DTO.dishMetadata.DishMetadataDTO;
import com.example.backoffice.DTO.dishMetadata.DishMetadataForGetResponseDTO;
import com.example.backoffice.DTO.dishMetadata.DishMetadataResponseDTO;
import com.example.backoffice.DTO.dishMetadata.DishMetadataUpdateDTO;
import com.example.backoffice.Entity.DishMetadata;
import com.example.backoffice.Exception.notFoundException.DishMetadataNotFoundException;
import com.example.backoffice.Mapper.DishMetadataMapper;
import com.example.backoffice.Repository.DishMetadataRepository;

@Service
public class DishMetadataServiceImp implements DishMetadataService {
    @Autowired
    private DishMetadataRepository dishMetadataRepository;
    @Autowired
    private DishMetadataMapper dishMetadataMapper;

    public DishMetadataServiceImp(
        DishMetadataRepository dishMetadataRepository,
        DishMetadataMapper dishMetadataMapper
    ) {
        this.dishMetadataRepository = dishMetadataRepository;
        this.dishMetadataMapper = dishMetadataMapper;
    }
    
    public List<DishMetadataForGetResponseDTO> getAll() {
        return dishMetadataRepository.findAll().stream().map(dishMetadataMapper::toForGetResponseDTO).toList();
    }

    public DishMetadataResponseDTO create(DishMetadataDTO payload) {
        DishMetadata dishMetadata = new DishMetadata();
        dishMetadata.setName(payload.getName().toLowerCase());
        if (payload.getDescription() != null) {
            dishMetadata.setDescription(payload.getDescription().toLowerCase());
        }
        dishMetadata.setPrice(payload.getPrice());
        DishMetadata entity = dishMetadataRepository.save(dishMetadata);
        return dishMetadataMapper.toResponseDTO(entity);
    }

    public DishMetadataForGetResponseDTO getById(String id) throws DishMetadataNotFoundException {
        DishMetadata dishMetadata = getEntityById(id);
        return dishMetadataMapper.toForGetResponseDTO(dishMetadata);
    }

    @Transactional
    public DishMetadataResponseDTO updateById(String id, DishMetadataUpdateDTO payload) throws DishMetadataNotFoundException {
        DishMetadata dishMetadata = getEntityById(id);
        dishMetadataMapper.updateFromDto(payload, dishMetadata);
        return dishMetadataMapper.toResponseDTO(dishMetadataRepository.save(dishMetadata));
    }

    @Transactional
    public void deleteById(String id) throws DishMetadataNotFoundException {
        DishMetadata dishMetadata = getEntityById(id);
        dishMetadataRepository.delete(dishMetadata);
    }

    public DishMetadata getEntityById(String id) {
        return dishMetadataRepository.findById(id).orElseThrow(() -> new DishMetadataNotFoundException(id));
    }

}
