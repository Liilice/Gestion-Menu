package com.example.backoffice.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.backoffice.DTO.ingredient.IngredientDTO;
import com.example.backoffice.DTO.ingredient.IngredientResponseDTO;
import com.example.backoffice.DTO.ingredient.IngredientUpdateDTO;
import com.example.backoffice.Entity.Category;
import com.example.backoffice.Entity.Ingredient;
import com.example.backoffice.Exception.notFoundException.CategoryNotFoundException;
import com.example.backoffice.Exception.notFoundException.IngredientNotFoundException;
import com.example.backoffice.Mapper.IngredientMapper;
import com.example.backoffice.Repository.IngredientRepository;

@ExtendWith(MockitoExtension.class)
public class IngredientServiceTests {
    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private CategoryService categoryService;

    private IngredientMapper ingredientMapper;

    private IngredientServiceImp ingredientServiceImp;

    private Ingredient ingredient;

    private Ingredient ingredient2;

    private Category category;

    private Category category2;

    @BeforeEach
    void setup() {
        ingredientMapper = Mappers.getMapper(IngredientMapper.class);

        ingredientServiceImp = new IngredientServiceImp();

        ReflectionTestUtils.setField(ingredientServiceImp, "ingredientRepository", ingredientRepository);
        ReflectionTestUtils.setField(ingredientServiceImp, "categoryService", categoryService);
        ReflectionTestUtils.setField(ingredientServiceImp, "ingredientMapper", ingredientMapper);

        // ==================== CATEGORY ====================
        category = new Category();
        category.setId("légumes");
        category.setName("légumes");

        category2 = new Category();
        category2.setId("protéine");
        category2.setName("protéine");

        // ==================== INGREDIENT ====================
        ingredient = new Ingredient();
        ingredient.setId("ingredient-1");
        ingredient.setName("brocoli");
        ingredient.setCategory(category);

        ingredient2 = new Ingredient();
        ingredient2.setId("ingredient-1");
        ingredient2.setName("chou");
        ingredient2.setCategory(category);
    }

    @Test
    void shouldGetAll() {
        when(ingredientRepository.findAll()).thenReturn(List.of(ingredient, ingredient2));

        List<IngredientResponseDTO> result = ingredientServiceImp.getAll();

        assertThat(result)
                .hasSize(2)
                .extracting(IngredientResponseDTO::getName)
                .containsExactlyInAnyOrder("brocoli", "chou");
    }

    @Test
    void shouldCreate() {
        Ingredient savedIngredient = new Ingredient();
        savedIngredient.setId("courgette");
        savedIngredient.setName("courgette");
        savedIngredient.setCategory(category);

        IngredientDTO payload = new IngredientDTO();
        payload.setName("courgette");
        payload.setCategoryId(category.getId());

        when(categoryService.getEntityById(category.getId())).thenReturn(category);

        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(savedIngredient);

        IngredientResponseDTO result = ingredientServiceImp.create(payload);

        assertThat(result.getName()).isEqualTo(payload.getName());
        assertThat(result.getCategoryId()).isEqualTo(category.getId());
        assertThat(result.getCategoryName()).isEqualTo(category.getName());

        verify(categoryService).getEntityById(category.getId());
        verify(ingredientRepository).save(any(Ingredient.class));
    }

    @Test
    void shouldCreateWithoutCategory() {
        Ingredient savedIngredient = new Ingredient();
        savedIngredient.setId("courgette");
        savedIngredient.setName("courgette");

        IngredientDTO payload = new IngredientDTO();
        payload.setName("courgette");

        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(savedIngredient);

        IngredientResponseDTO result = ingredientServiceImp.create(payload);

        assertThat(result.getName()).isEqualTo(payload.getName());
        assertThat(result.getCategoryId()).isNullOrEmpty();
        assertThat(result.getCategoryName()).isNullOrEmpty();

        verify(categoryService, never()).getEntityById(any());
        verify(ingredientRepository).save(any(Ingredient.class));
    }

    @Test
    void shouldGetById() {
        String id = ingredient.getId();

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(ingredient));

        IngredientResponseDTO result = ingredientServiceImp.getById(id);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo(ingredient.getName());
        assertThat(result.getCategoryId()).isEqualTo(ingredient.getCategory().getId());
        assertThat(result.getCategoryName()).isEqualTo(ingredient.getCategory().getName());

        verify(ingredientRepository).findById(id);
    }

    @Test
    void shouldThrowWithUnknownId() {
        String id = "non-existing-id";

        when(ingredientRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ingredientServiceImp.getById(id))
                .isInstanceOf(IngredientNotFoundException.class)
                .hasMessageContaining(id);

        verify(ingredientRepository).findById(id);
    }

    @Test
    void shouldUpdateById() {
        String id = ingredient.getId();

        IngredientUpdateDTO payload = new IngredientUpdateDTO();
        payload.setName("carotte");
        payload.setCategoryId(category2.getId());

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(ingredient));
        when(categoryService.getEntityById(payload.getCategoryId())).thenReturn(category2);
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);

        IngredientResponseDTO result = ingredientServiceImp.updateById(id, payload);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo(payload.getName());
        assertThat(result.getCategoryId()).isEqualTo(category2.getId());
        assertThat(result.getCategoryName()).isEqualTo(category2.getName());

        verify(categoryService).getEntityById(category2.getId());
        verify(ingredientRepository).findById(id);
        verify(ingredientRepository).save(any(Ingredient.class));
    }

    @Test
    void shouldUpdateByIdWithoutUnknowCategory() {
        String id = ingredient.getId();

        IngredientUpdateDTO payload = new IngredientUpdateDTO();
        payload.setCategoryId("unknow-category-id");

        when(ingredientRepository.findById(id))
                .thenReturn(Optional.of(ingredient));

        when(categoryService.getEntityById(payload.getCategoryId()))
                .thenThrow(new CategoryNotFoundException(payload.getCategoryId()));

        assertThatThrownBy(() -> ingredientServiceImp.updateById(id, payload))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining(payload.getCategoryId());

        verify(ingredientRepository).findById(id);
        verify(categoryService).getEntityById(payload.getCategoryId());
    }

    @Test
    void shouldUpdateByIdWitoutCategory() {
        String id = ingredient.getId();

        IngredientUpdateDTO payload = new IngredientUpdateDTO();
        payload.setName("carotte");

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(ingredient));
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);

        IngredientResponseDTO result = ingredientServiceImp.updateById(id, payload);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo(payload.getName());
        assertThat(result.getCategoryId()).isEqualTo(category.getId());
        assertThat(result.getCategoryName()).isEqualTo(category.getName());

        verify(categoryService, never()).getEntityById(any());
        verify(ingredientRepository).findById(id);
        verify(ingredientRepository).save(any(Ingredient.class));
    }

    @Test
    void shouldDeleteById() {
        String id = ingredient.getId();

        when(ingredientRepository.findById(id))
                .thenReturn(Optional.of(ingredient));

        ingredientServiceImp.deleteById(id);

        verify(ingredientRepository).findById(id);
        verify(ingredientRepository).delete(ingredient);
    }
}
