package com.example.backoffice.Mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.backoffice.DTO.category.CategoryResponseDTO;
import com.example.backoffice.DTO.ingredient.IngredientSummaryDTO;
import com.example.backoffice.Entity.Category;
import com.example.backoffice.Entity.Ingredient;

public class CategoryMapperTests {
    private CategoryMapper categoryMapper;

    @Mock
    private IngredientMapper ingredientMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        categoryMapper = new CategoryMapper();
        categoryMapper.ingredientMapper = ingredientMapper;
    }

    @Test
    public void shouldMapCategoryWithoutIngredients() {
        Category category = new Category();
        category.setId("1L");
        category.setName("Test Category");

        CategoryResponseDTO dto = categoryMapper.toResponseDTO(category);

        assert dto.getId().equals("1L");
        assert dto.getName().equals("Test Category");
        assert dto.getIngredients().isEmpty();
    }

    @Test
    public void shouldMapCategoryWithIngredient() {
        Category category = new Category();
        category.setId("1L");
        category.setName("Test Category");

        Ingredient ingredient = new Ingredient();
        ingredient.setId("10L");
        ingredient.setName("Test Ingredient");
        ingredient.setCategory(category);

        category.setIngredients(List.of(ingredient));

        IngredientSummaryDTO ingredientDTO = new IngredientSummaryDTO();
        ingredientDTO.setId("10L");
        ingredientDTO.setName("Test Ingredient");

        when(ingredientMapper.toIngredientSummaryDTO(ingredient))
                .thenReturn(ingredientDTO);

        CategoryResponseDTO dto = categoryMapper.toResponseDTO(category);

        assertEquals(1, dto.getIngredients().size());
        assertEquals("10L", dto.getIngredients().get(0).getId());
        assertEquals("Test Ingredient", dto.getIngredients().get(0).getName());
    }
}
