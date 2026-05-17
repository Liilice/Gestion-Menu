package com.example.backoffice.Service.Integration;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.backoffice.DTO.ingredient.IngredientDTO;
import com.example.backoffice.DTO.ingredient.IngredientResponseDTO;
import com.example.backoffice.DTO.ingredient.IngredientUpdateDTO;
import com.example.backoffice.Entity.Category;
import com.example.backoffice.Entity.Ingredient;
import com.example.backoffice.Exception.notFoundException.CategoryNotFoundException;
import com.example.backoffice.Exception.notFoundException.IngredientNotFoundException;
import com.example.backoffice.Repository.CategoryRepository;
import com.example.backoffice.Repository.IngredientRepository;
import com.example.backoffice.Service.IngredientServiceImp;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class IngredientServiceIntegrationTests {
    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IngredientServiceImp ingredientServiceImp;

    private Category category1;
    private Category category2;

    private Ingredient ingredient1;
    private Ingredient ingredient2;

    private String unknownId;

    @BeforeEach
    void setup() {
        unknownId = "non-existing-id";

        ingredientRepository.deleteAll();
        categoryRepository.deleteAll();

        category1 = createCategory("légumes");
        category2 = createCategory("protéines");

        ingredient1 = createIngredient("carotte", category1);
        ingredient2 = createIngredient("poulet", category2);
    }

    @Test
    void shouldGetAll() {
        List<IngredientResponseDTO> result = ingredientServiceImp.getAll();

        assertThat(result)
                .hasSize(2)
                .extracting(IngredientResponseDTO::getName)
                .containsExactlyInAnyOrder(
                        ingredient1.getName(),
                        ingredient2.getName()
                );
    }

    @Test
    void shouldCreateWithoutCategory() {
        IngredientDTO payload = new IngredientDTO();
        payload.setName("Tomate");

        IngredientResponseDTO result = ingredientServiceImp.create(payload);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("tomate");
        assertThat(result.getCategoryId()).isNull();
        assertThat(result.getCategoryName()).isNull();

        Ingredient saved = ingredientRepository.findById(result.getId()).orElseThrow();

        assertThat(saved.getName()).isEqualTo("tomate");
        assertThat(saved.getCategory()).isNull();
    }

    @Test
    void shouldCreateWithCategory() {
        IngredientDTO payload = new IngredientDTO();
        payload.setName("Saumon");
        payload.setCategoryId(category2.getId());

        IngredientResponseDTO result = ingredientServiceImp.create(payload);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("saumon");
        assertThat(result.getCategoryId()).isEqualTo(category2.getId());
        assertThat(result.getCategoryName()).isEqualTo(category2.getName());

        Ingredient saved = ingredientRepository.findById(result.getId()).orElseThrow();

        assertThat(saved.getName()).isEqualTo("saumon");
        assertThat(saved.getCategory()).isNotNull();
        assertThat(saved.getCategory().getId()).isEqualTo(category2.getId());
        assertThat(saved.getCategory().getName()).isEqualTo(category2.getName());
    }

    @Test
    void shouldThrowWhenCreateWithUnknownCategory() {
        IngredientDTO payload = new IngredientDTO();
        payload.setName("Saumon");
        payload.setCategoryId(unknownId);

        assertThat(categoryRepository.findById(unknownId)).isEmpty();

        assertThatThrownBy(() -> ingredientServiceImp.create(payload))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining(unknownId);
    }

    @Test
    void shouldGetById() {
        IngredientResponseDTO result = ingredientServiceImp.getById(ingredient1.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(ingredient1.getId());
        assertThat(result.getName()).isEqualTo(ingredient1.getName());
        assertThat(result.getCategoryId()).isEqualTo(category1.getId());
        assertThat(result.getCategoryName()).isEqualTo(category1.getName());
    }

    @Test
    void shouldThrowWhenGetByIdUnknown() {
        assertThat(ingredientRepository.findById(unknownId)).isEmpty();

        assertThatThrownBy(() -> ingredientServiceImp.getById(unknownId))
                .isInstanceOf(IngredientNotFoundException.class)
                .hasMessageContaining(unknownId);
    }

    @Test
    void shouldUpdateNameOnly() {
        IngredientUpdateDTO payload = new IngredientUpdateDTO();
        payload.setName("Carotte jaune");

        IngredientResponseDTO result = ingredientServiceImp.updateById(ingredient1.getId(), payload);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(ingredient1.getId());
        assertThat(result.getName()).isEqualTo("Carotte jaune");
        assertThat(result.getCategoryId()).isEqualTo(category1.getId());
        assertThat(result.getCategoryName()).isEqualTo(category1.getName());

        Ingredient saved = ingredientRepository.findById(ingredient1.getId()).orElseThrow();

        assertThat(saved.getName()).isEqualTo("Carotte jaune");
        assertThat(saved.getCategory().getId()).isEqualTo(category1.getId());
    }

    @Test
    void shouldUpdateCategoryOnly() {
        IngredientUpdateDTO payload = new IngredientUpdateDTO();
        payload.setCategoryId(category2.getId());

        IngredientResponseDTO result = ingredientServiceImp.updateById(ingredient1.getId(), payload);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(ingredient1.getId());
        assertThat(result.getName()).isEqualTo(ingredient1.getName());
        assertThat(result.getCategoryId()).isEqualTo(category2.getId());
        assertThat(result.getCategoryName()).isEqualTo(category2.getName());

        Ingredient saved = ingredientRepository.findById(ingredient1.getId()).orElseThrow();

        assertThat(saved.getName()).isEqualTo(ingredient1.getName());
        assertThat(saved.getCategory().getId()).isEqualTo(category2.getId());
    }

    @Test
    void shouldUpdateNameAndCategory() {
        IngredientUpdateDTO payload = new IngredientUpdateDTO();
        payload.setName("Tofu");
        payload.setCategoryId(category2.getId());

        IngredientResponseDTO result = ingredientServiceImp.updateById(ingredient1.getId(), payload);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(ingredient1.getId());
        assertThat(result.getName()).isEqualTo("Tofu");
        assertThat(result.getCategoryId()).isEqualTo(category2.getId());
        assertThat(result.getCategoryName()).isEqualTo(category2.getName());

        Ingredient saved = ingredientRepository.findById(ingredient1.getId()).orElseThrow();

        assertThat(saved.getName()).isEqualTo("Tofu");
        assertThat(saved.getCategory().getId()).isEqualTo(category2.getId());
    }

    @Test
    void shouldThrowWhenUpdateUnknownIngredient() {
        IngredientUpdateDTO payload = new IngredientUpdateDTO();
        payload.setName("Tofu");

        assertThatThrownBy(() -> ingredientServiceImp.updateById(unknownId, payload))
                .isInstanceOf(IngredientNotFoundException.class)
                .hasMessageContaining(unknownId);
    }

    @Test
    void shouldThrowWhenUpdateWithUnknownCategory() {
        IngredientUpdateDTO payload = new IngredientUpdateDTO();
        payload.setCategoryId(unknownId);

        assertThatThrownBy(() -> ingredientServiceImp.updateById(ingredient1.getId(), payload))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining(unknownId);
    }

    @Test
    void shouldDeleteById() {
        String id = ingredient1.getId();

        ingredientServiceImp.deleteById(id);

        assertThat(ingredientRepository.existsById(id)).isFalse();
    }

    @Test
    void shouldThrowWhenDeleteUnknownId() {
        assertThatThrownBy(() -> ingredientServiceImp.deleteById(unknownId))
                .isInstanceOf(IngredientNotFoundException.class)
                .hasMessageContaining(unknownId);
    }

    private Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    private Ingredient createIngredient(String name, Category category) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setCategory(category);
        return ingredientRepository.save(ingredient);
    }
}