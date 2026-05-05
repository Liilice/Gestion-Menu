package com.example.backoffice.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backoffice.DTO.dishRelation.DishRelationDTO;
import com.example.backoffice.DTO.dishRelation.DishRelationResponseDTO;
import com.example.backoffice.DTO.dishRelation.DishRelationUpdateDTO;
import com.example.backoffice.Entity.DishMetadata;
import com.example.backoffice.Entity.DishRelation;
import com.example.backoffice.Entity.Ingredient;
import com.example.backoffice.Entity.Label;
import com.example.backoffice.Exception.dataNotValidException.DishRelationDataNotValidException;
import com.example.backoffice.Exception.notFoundException.DishRelationNotFoundException;
import com.example.backoffice.Mapper.DishRelationMapper;
import com.example.backoffice.Repository.DishRelationRepository;

@Service
public class DishRelationServiceImp implements DishRelationService {
    @Autowired
    private DishRelationRepository dishRelationRepository;
    @Autowired
    private DishMetadataService dishMetadataService;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private LabelService labelService;
    @Autowired
    private DishRelationMapper dishRelationMapper;

    public List<DishRelationResponseDTO> getAll() {
        return dishRelationRepository.findAll().stream()
                .map(dishRelation -> dishRelationMapper.toResponseDTO(dishRelation))
                .toList();
    }

    public DishRelationResponseDTO create(DishRelationDTO payload) {
        if (payload.getIngredientId() == null && payload.getLabelId() == null) {
            throw new DishRelationDataNotValidException();
        }
        DishRelation dishRelation = new DishRelation();
        DishMetadata dishMetadata = dishMetadataService.getEntityById(payload.getDishMetadataId());
        dishRelation.setDishMetadata(dishMetadata);
        if (payload.getIngredientId() != null) {
            Ingredient ingredient = ingredientService.getEntityById(payload.getIngredientId());
            dishRelation.setIngredient(ingredient);
        }
        if (payload.getLabelId() != null) {
            Label label = labelService.getEntityById(payload.getLabelId());
            dishRelation.setLabel(label);
        }
        return dishRelationMapper.toResponseDTO(dishRelationRepository.save(dishRelation));
    }

    public DishRelationResponseDTO getById(String id) throws DishRelationNotFoundException {
        DishRelation dishRelation = getEntityById(id);
        return dishRelationMapper.toResponseDTO(dishRelation);
    }

    public DishRelationResponseDTO updateById(String id, DishRelationUpdateDTO payload) {
        DishRelation dishRelation = getEntityById(id);

        if (payload.getDishMetadataId() != null) {
            DishMetadata dishMetadata = dishMetadataService.getEntityById(payload.getDishMetadataId());
            dishRelation.setDishMetadata(dishMetadata);
        }
        if (payload.getIngredientId() != null) {
            Ingredient ingredient = ingredientService.getEntityById(payload.getIngredientId());
            dishRelation.setIngredient(ingredient);
        }
        if (payload.getLabelId() != null) {
            Label label = labelService.getEntityById(payload.getLabelId());
            dishRelation.setLabel(label);
        }
        return dishRelationMapper.toResponseDTO(dishRelationRepository.save(dishRelation));
    }

    public void deleteById(String id) throws DishRelationNotFoundException {
        DishRelation dishRelation = getEntityById(id);
        dishRelationRepository.delete(dishRelation);
    }

    public DishRelation getEntityById(String id) throws DishRelationNotFoundException {
        return dishRelationRepository.findById(id).orElseThrow(() -> new DishRelationNotFoundException(id));
    }
}
