package com.example.backoffice.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import com.example.backoffice.DTO.category.CategoryDTO;
import com.example.backoffice.DTO.category.CategoryResponseDTO;
import com.example.backoffice.Entity.Category;
import com.example.backoffice.Entity.Ingredient;
import com.example.backoffice.Exception.notFoundException.CategoryNotFoundException;
import com.example.backoffice.Repository.CategoryRepository;
import com.example.backoffice.Repository.IngredientRepository;

import jakarta.persistence.EntityManager;

import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CategoryServiceIntegrationTests {
    @Autowired
    private CategoryServiceImp categoryServiceImp;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setup() {
        categoryRepository.deleteAll();

        Category category1 = new Category();
        category1.setName("légumes");
        categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName("protéines");
        categoryRepository.save(category2);
    }

    @Test
    void shouldGetAll() {
        // When
        List<CategoryResponseDTO> result = categoryServiceImp.getAll();

        // Then
        assertThat(result)
                .hasSize(2)
                .extracting(CategoryResponseDTO::getName)
                .containsExactly("légumes", "protéines");
    }

    @Test
    void shouldCreateCategory() {
        // Given
        CategoryDTO payload = new CategoryDTO();
        payload.setName("fruits");

        // When
        CategoryResponseDTO result = categoryServiceImp.create(payload);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("fruits");

        Category categoryInDatabase = categoryRepository.findById(result.getId())
                .orElseThrow();

        assertThat(categoryInDatabase.getName()).isEqualTo("fruits");
    }

    @Test
    void shouldGetById() {
        // Given
        Category category = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equals("légumes"))
                .findFirst()
                .orElseThrow();
        // When
        CategoryResponseDTO result = categoryServiceImp.getById(category.getId());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(category.getId());
        assertThat(result.getName()).isEqualTo(category.getName());
    }

    @Test
    void shouldThrowCategoryNotFound() {
        // Given
        String nonExistingId = "non-existing-id";

        // When & Then
        try {
            categoryServiceImp.getById(nonExistingId);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(CategoryNotFoundException.class);
            assertThat(e.getMessage()).contains(nonExistingId);
        }

        assertThatThrownBy(() -> categoryServiceImp.getById(nonExistingId))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining(nonExistingId);
    }

    @Test
    void shouldUpdateById() {
        // Given
        Category category = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equals("légumes"))
                .findFirst()
                .orElseThrow();
        CategoryDTO payload = new CategoryDTO();
        payload.setName("légumes frais");

        // When
        CategoryResponseDTO result = categoryServiceImp.updateById(category.getId(), payload);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(category.getId());
        assertThat(result.getName()).isEqualTo("légumes frais");

        Category categoryInDatabase = categoryRepository.findById(category.getId())
                .orElseThrow();

        assertThat(categoryInDatabase.getName()).isEqualTo("légumes frais");
    }

    @Test
    void shouldUpdateByIdNotFound() {
        // Given
        String nonExistingId = "non-existing-id";
        CategoryDTO payload = new CategoryDTO();
        payload.setName("légumes frais");

        // When & Then
        assertThatThrownBy(() -> categoryServiceImp.updateById(nonExistingId, payload))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining(nonExistingId);
    }

    @Test
    void shouldDeleteById() {
        // Given
        Category category = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equals("légumes"))
                .findFirst()
                .orElseThrow();
        // When
        categoryServiceImp.deleteById(category.getId());

        // Then
        boolean exists = categoryRepository.existsById(category.getId());
        assertThat(exists).isFalse();
    }

    @Test
    void shouldDeleteByIdWithIngredients() {
        // Given
        Category category = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equals("légumes"))
                .findFirst()
                .orElseThrow();

        Ingredient ingredient = new Ingredient();
        ingredient.setName("carotte");
        ingredient.setCategory(category);

        Category category2 = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equals("protéines"))
                .findFirst()
                .orElseThrow();
        Ingredient ingredientCategory2 = new Ingredient();
        ingredientCategory2.setName("poulet");
        ingredientCategory2.setCategory(category2);

        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        Ingredient savedIngredientCategory2 = ingredientRepository.save(ingredientCategory2);

        entityManager.flush();
        entityManager.clear();

        // When
        categoryServiceImp.deleteById(category.getId());

        entityManager.flush();
        entityManager.clear();

        // Then
        Ingredient ingredientEntity = ingredientRepository.findById(savedIngredient.getId())
                .orElseThrow();
        Ingredient ingredientCategory2Entity = ingredientRepository.findById(savedIngredientCategory2.getId())
                .orElseThrow();

        assertThat(ingredientEntity.getCategory()).isNull();
        assertThat(ingredientCategory2Entity.getCategory()).isNotNull();
        assertThat(categoryRepository.existsById(category.getId())).isFalse();
    }

    @Test
    public void shouldDeleteByIdNotFound() {
        // Given
        String nonExistingId = "non-existing-id";
        // When & Then
        assertThatThrownBy(() -> categoryServiceImp.deleteById(nonExistingId))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining(nonExistingId);
    }

}
