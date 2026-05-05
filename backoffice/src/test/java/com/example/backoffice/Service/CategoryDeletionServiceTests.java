package com.example.backoffice.Service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.backoffice.Entity.Category;
import com.example.backoffice.Entity.Ingredient;
import com.example.backoffice.Exception.notFoundException.IngredientNotFoundException;
import com.example.backoffice.Repository.IngredientRepository;

@ExtendWith(MockitoExtension.class)
public class CategoryDeletionServiceTests {
    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private CategoryDeletionService categoryDeletionService;

    private Ingredient brocoli;
    private Category category;

    private Ingredient createIngredient(String name) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId("id-" + name.toLowerCase());
        ingredient.setName(name.toLowerCase());
        ingredient.setCategory(category);
        return ingredient;
    }

    @BeforeEach
    public void setup() {
        category = new Category();
        category.setId("id-légumes");
        category.setName("légumes");

        brocoli = createIngredient("brocoli");
    }

    @Test
    public void setCategoryNullByIngredientId() {
        when(ingredientRepository.findById(brocoli.getId())).thenReturn(Optional.of(brocoli));

        categoryDeletionService.setCategoryNull(brocoli.getId());

        assertThat(brocoli.getCategory()).isNull();
        verify(ingredientRepository).findById(brocoli.getId());
        verify(ingredientRepository).save(brocoli);
    }

    @Test
    public void shouldThrowWhenIngredientNotFound() {
        String id = "non-existing-id";
        when(ingredientRepository.findById(id))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> categoryDeletionService.setCategoryNull(id))
                .isInstanceOf(IngredientNotFoundException.class)
                .hasMessage("Ingredient not found with id: " + id);

    }
}
