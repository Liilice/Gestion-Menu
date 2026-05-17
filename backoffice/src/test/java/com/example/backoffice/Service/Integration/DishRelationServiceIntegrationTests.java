package com.example.backoffice.Service.Integration;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.backoffice.DTO.dishRelation.DishRelationDTO;
import com.example.backoffice.DTO.dishRelation.DishRelationResponseDTO;
import com.example.backoffice.DTO.dishRelation.DishRelationUpdateDTO;
import com.example.backoffice.Entity.Category;
import com.example.backoffice.Entity.DishMetadata;
import com.example.backoffice.Entity.DishRelation;
import com.example.backoffice.Entity.Ingredient;
import com.example.backoffice.Entity.Label;
import com.example.backoffice.Exception.dataNotValidException.DishRelationDataNotValidException;
import com.example.backoffice.Exception.notFoundException.DishRelationNotFoundException;
import com.example.backoffice.Repository.CategoryRepository;
import com.example.backoffice.Repository.DishMetadataRepository;
import com.example.backoffice.Repository.DishRelationRepository;
import com.example.backoffice.Repository.IngredientRepository;
import com.example.backoffice.Repository.LabelRepository;
import com.example.backoffice.Service.DishRelationServiceImp;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DishRelationServiceIntegrationTests {
    @Autowired
    private DishRelationRepository dishRelationRepository;

    @Autowired
    private DishMetadataRepository dishMetadataRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DishRelationServiceImp dishRelationServiceImp;

    private DishMetadata dishMetadata1;
    private DishMetadata dishMetadata2;
    private Ingredient ingredient1;
    private Ingredient ingredient2;
    private Label label1;
    private Label label2;
    private DishRelation relation1;
    private String unknownId;

    @BeforeEach
    void setup() {
        unknownId = "non-existing-id";

        dishRelationRepository.deleteAll();
        ingredientRepository.deleteAll();
        labelRepository.deleteAll();
        dishMetadataRepository.deleteAll();
        categoryRepository.deleteAll();

        Category category = createCategory("légumes");

        dishMetadata1 = createDishMetadata("bibimbap", "plat coréen", BigDecimal.valueOf(15.50));
        dishMetadata2 = createDishMetadata("bao", "pain vapeur", BigDecimal.valueOf(6.50));

        ingredient1 = createIngredient("carotte", category);
        ingredient2 = createIngredient("poulet", category);

        label1 = createLabel("vegan");
        label2 = createLabel("spicy");

        relation1 = createDishRelation(dishMetadata1, ingredient1, null);
    }

    @Test
    void shouldGetAll() {
        List<DishRelationResponseDTO> result = dishRelationServiceImp.getAll();

        assertThat(result).hasSize(1);
        assertThat(result)
                .extracting(DishRelationResponseDTO::getId)
                .containsExactly(relation1.getId());
    }

    @Test
    void shouldCreateWithIngredient() {
        DishRelationDTO payload = new DishRelationDTO();
        payload.setDishMetadataId(dishMetadata1.getId());
        payload.setIngredientId(ingredient2.getId());

        DishRelationResponseDTO result = dishRelationServiceImp.create(payload);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();

        DishRelation saved = dishRelationRepository.findById(result.getId()).orElseThrow();

        assertThat(saved.getDishMetadata().getId()).isEqualTo(dishMetadata1.getId());
        assertThat(saved.getIngredient()).isNotNull();
        assertThat(saved.getIngredient().getId()).isEqualTo(ingredient2.getId());
        assertThat(saved.getLabel()).isNull();
    }

    @Test
    void shouldCreateWithLabel() {
        DishRelationDTO payload = new DishRelationDTO();
        payload.setDishMetadataId(dishMetadata1.getId());
        payload.setLabelId(label1.getId());

        DishRelationResponseDTO result = dishRelationServiceImp.create(payload);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();

        DishRelation saved = dishRelationRepository.findById(result.getId()).orElseThrow();

        assertThat(saved.getDishMetadata().getId()).isEqualTo(dishMetadata1.getId());
        assertThat(saved.getLabel()).isNotNull();
        assertThat(saved.getLabel().getId()).isEqualTo(label1.getId());
        assertThat(saved.getIngredient()).isNull();
    }

    @Test
    void shouldThrowWhenCreateWithoutIngredientAndLabel() {
        DishRelationDTO payload = new DishRelationDTO();
        payload.setDishMetadataId(dishMetadata1.getId());

        assertThatThrownBy(() -> dishRelationServiceImp.create(payload))
                .isInstanceOf(DishRelationDataNotValidException.class);
    }

    @Test
    void shouldGetById() {
        DishRelationResponseDTO result = dishRelationServiceImp.getById(relation1.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(relation1.getId());
    }

    @Test
    void shouldThrowWhenGetByIdUnknown() {
        assertThat(dishRelationRepository.findById(unknownId)).isEmpty();

        assertThatThrownBy(() -> dishRelationServiceImp.getById(unknownId))
                .isInstanceOf(DishRelationNotFoundException.class)
                .hasMessageContaining(unknownId);
    }

    @Test
    void shouldUpdateById() {
        DishRelationUpdateDTO payload = new DishRelationUpdateDTO();
        payload.setDishMetadataId(dishMetadata2.getId());
        payload.setIngredientId(ingredient2.getId());
        payload.setLabelId(label2.getId());

        DishRelationResponseDTO result = dishRelationServiceImp.updateById(relation1.getId(), payload);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(relation1.getId());

        DishRelation updated = dishRelationRepository.findById(relation1.getId()).orElseThrow();

        assertThat(updated.getDishMetadata().getId()).isEqualTo(dishMetadata2.getId());
        assertThat(updated.getIngredient().getId()).isEqualTo(ingredient2.getId());
        assertThat(updated.getLabel().getId()).isEqualTo(label2.getId());
    }

    @Test
    void shouldThrowWhenUpdateUnknownId() {
        DishRelationUpdateDTO payload = new DishRelationUpdateDTO();
        payload.setIngredientId(ingredient2.getId());

        assertThatThrownBy(() -> dishRelationServiceImp.updateById(unknownId, payload))
                .isInstanceOf(DishRelationNotFoundException.class)
                .hasMessageContaining(unknownId);
    }

    @Test
    void shouldDeleteById() {
        String id = relation1.getId();

        dishRelationServiceImp.deleteById(id);

        assertThat(dishRelationRepository.existsById(id)).isFalse();
    }

    @Test
    void shouldThrowWhenDeleteUnknownId() {
        assertThatThrownBy(() -> dishRelationServiceImp.deleteById(unknownId))
                .isInstanceOf(DishRelationNotFoundException.class)
                .hasMessageContaining(unknownId);
    }

    private DishMetadata createDishMetadata(String name, String description, BigDecimal price) {
        DishMetadata entity = new DishMetadata();
        entity.setName(name);
        entity.setDescription(description);
        entity.setPrice(price);
        return dishMetadataRepository.save(entity);
    }

    private Category createCategory(String name) {
        Category entity = new Category();
        entity.setName(name);
        return categoryRepository.save(entity);
    }

    private Ingredient createIngredient(String name, Category category) {
        Ingredient entity = new Ingredient();
        entity.setName(name);
        entity.setCategory(category);
        return ingredientRepository.save(entity);
    }

    private Label createLabel(String name) {
        Label entity = new Label();
        entity.setName(name);
        return labelRepository.save(entity);
    }

    private DishRelation createDishRelation(DishMetadata dishMetadata, Ingredient ingredient, Label label) {
        DishRelation entity = new DishRelation();
        entity.setDishMetadata(dishMetadata);
        entity.setIngredient(ingredient);
        entity.setLabel(label);
        return dishRelationRepository.save(entity);
    }
}