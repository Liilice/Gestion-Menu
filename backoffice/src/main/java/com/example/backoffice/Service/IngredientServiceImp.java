package com.example.backoffice.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backoffice.DTO.ingredient.IngredientDTO;
import com.example.backoffice.DTO.ingredient.IngredientResponseDTO;
import com.example.backoffice.DTO.ingredient.IngredientUpdateDTO;
import com.example.backoffice.Entity.Category;
import com.example.backoffice.Entity.Ingredient;
import com.example.backoffice.Exception.notFoundException.CategoryNotFoundException;
import com.example.backoffice.Exception.notFoundException.IngredientNotFoundException;
import com.example.backoffice.Mapper.IngredientMapper;
import com.example.backoffice.Repository.IngredientRepository;

@Service
public class IngredientServiceImp implements IngredientService {
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private IngredientMapper ingredientMapper;
    private static final Logger logger = LoggerFactory.getLogger(IngredientServiceImp.class);

    public List<IngredientResponseDTO> getAll() {
        return ingredientRepository.findAll().stream()
                .map(ingredientMapper::toResponseDTO)
                .toList();
    }

    public IngredientResponseDTO create(IngredientDTO ingredientDTO) throws CategoryNotFoundException {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientDTO.getName().toLowerCase());
        if (ingredientDTO.getCategoryId() != null) {
            Category category = categoryService.getEntityById(ingredientDTO.getCategoryId());
            ingredient.setCategory(category);
        }
        return ingredientMapper.toResponseDTO(ingredientRepository.save(ingredient));
    }

    public IngredientResponseDTO getById(String id) throws IngredientNotFoundException {
        Ingredient ingredient = getEntityById(id);
        return ingredientMapper.toResponseDTO(ingredient);
    }

    @Transactional
    public IngredientResponseDTO updateById(String id, IngredientUpdateDTO dto)
            throws IngredientNotFoundException, CategoryNotFoundException {
        Ingredient ingredient = getEntityById(id);
        ingredientMapper.updateFromDto(dto, ingredient);
        if (dto.getCategoryId() != null) {
            Category category = categoryService.getEntityById(dto.getCategoryId());
            ingredient.setCategory(category);
        }
        return ingredientMapper.toResponseDTO(
                ingredientRepository.save(ingredient));
    }

    @Transactional
    public void deleteById(String id) throws IngredientNotFoundException {
        Ingredient ingredient = getEntityById(id);
        ingredientRepository.delete(ingredient);
        logger.info("Deleted ingredient with id: {}", id);
    }

    public Ingredient getEntityById(String id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new IngredientNotFoundException(id));
    }

}
