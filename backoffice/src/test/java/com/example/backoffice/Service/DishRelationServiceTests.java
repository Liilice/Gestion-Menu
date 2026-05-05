package com.example.backoffice.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.backoffice.DTO.dishRelation.DishRelationDTO;
import com.example.backoffice.DTO.dishRelation.DishRelationResponseDTO;
import com.example.backoffice.DTO.dishRelation.DishRelationUpdateDTO;
import com.example.backoffice.Entity.*;
import com.example.backoffice.Exception.dataNotValidException.DishRelationDataNotValidException;
import com.example.backoffice.Exception.notFoundException.DishRelationNotFoundException;
import com.example.backoffice.Mapper.DishRelationMapper;
import com.example.backoffice.Repository.DishRelationRepository;

@ExtendWith(MockitoExtension.class)
class DishRelationServiceTests {
    @Mock
    private DishRelationRepository dishRelationRepository;

    @Mock
    private DishMetadataServiceImp dishMetadataServiceImp;

    @Mock
    private IngredientServiceImp ingredientServiceImp;

    @Mock
    private LabelServiceImp labelServiceImp;

    private DishRelationMapper dishRelationMapper;

    private DishRelationServiceImp dishRelationServiceImp;

    private DishMetadata dishMetadata;
    private DishMetadata dishMetadata2;
    private Ingredient ingredient;
    private Ingredient ingredient2;
    private Label label;
    private Label label2;
    private DishRelation relation;

    @BeforeEach
    void setup() {
        dishRelationMapper = Mappers.getMapper(DishRelationMapper.class);

        dishRelationServiceImp = new DishRelationServiceImp();

        ReflectionTestUtils.setField(dishRelationServiceImp, "dishRelationRepository", dishRelationRepository);
        ReflectionTestUtils.setField(dishRelationServiceImp, "dishMetadataService", dishMetadataServiceImp);
        ReflectionTestUtils.setField(dishRelationServiceImp, "ingredientService", ingredientServiceImp);
        ReflectionTestUtils.setField(dishRelationServiceImp, "labelService", labelServiceImp);
        ReflectionTestUtils.setField(dishRelationServiceImp, "dishRelationMapper", dishRelationMapper);

        // ==================== DISH METADATA ====================
        dishMetadata = new DishMetadata();
        dishMetadata.setId("dish-1");
        dishMetadata.setName("bibimpap");

        dishMetadata2 = new DishMetadata();
        dishMetadata2.setId("dish-2");
        dishMetadata2.setName("bao");

        // ==================== INGREDIENT ====================
        ingredient = new Ingredient();
        ingredient.setId("ingredient-1");
        ingredient.setName("riz");

        ingredient2 = new Ingredient();
        ingredient2.setId("ingredient-2");
        ingredient2.setName("poulet");

        // ==================== LABEL ====================
        label = new Label();
        label.setId("label-1");
        label.setName("spicy");

        label2 = new Label();
        label2.setId("label-2");
        label2.setName("vegan");

        // ==================== RELATIONS ====================
        relation = new DishRelation();
        relation.setId("relation-1");
        relation.setDishMetadata(dishMetadata);
        relation.setIngredient(ingredient);
        relation.setLabel(label);
    }

    @Test
    void shouldGetAll() {
        when(dishRelationRepository.findAll()).thenReturn(List.of(relation));

        List<DishRelationResponseDTO> result = dishRelationServiceImp.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("relation-1");

        verify(dishRelationRepository).findAll();
    }

    @Test
    void shouldThrowDishRelationDataNotValidException() {
        DishRelation savedRelation = new DishRelation();
        savedRelation.setId("relation-2");
        savedRelation.setDishMetadata(dishMetadata);

        DishRelationDTO payload = new DishRelationDTO();
        payload.setDishMetadataId(dishMetadata.getId());
        payload.setIngredientId(null);
        payload.setLabelId(null);

        assertThatThrownBy(() -> dishRelationServiceImp.create(payload))
                .isInstanceOf(DishRelationDataNotValidException.class);

        verifyNoInteractions(dishRelationRepository);
        verifyNoInteractions(ingredientServiceImp);
        verifyNoInteractions(labelServiceImp);
        verifyNoInteractions(dishMetadataServiceImp);
    }

    @Test
    void shouldCreateWithoutIngredientId() {
        DishRelation savedRelation = new DishRelation();
        savedRelation.setId("relation-2");
        savedRelation.setDishMetadata(dishMetadata);
        savedRelation.setLabel(label);

        DishRelationDTO payload = new DishRelationDTO();
        payload.setDishMetadataId(dishMetadata.getId());
        payload.setLabelId(label.getId());

        when(dishMetadataServiceImp.getEntityById(dishMetadata.getId()))
                .thenReturn(dishMetadata);

        when(labelServiceImp.getEntityById(label.getId()))
                .thenReturn(label);

        when(dishRelationRepository.save(any(DishRelation.class)))
                .thenReturn(savedRelation);

        DishRelationResponseDTO result = dishRelationServiceImp.create(payload);

        assertThat(result.getDishMetadata().getId()).isEqualTo(dishMetadata.getId());
        assertThat(result.getIngredient()).isNull();
        assertThat(result.getLabel().getId()).isEqualTo(label.getId());

        verify(dishMetadataServiceImp).getEntityById(dishMetadata.getId());
        verify(labelServiceImp).getEntityById(label.getId());
        verify(ingredientServiceImp, never()).getEntityById(any());
        verify(dishRelationRepository).save(any(DishRelation.class));
    }

    @Test
    void shouldCreateWithoutLabelId() {
        DishRelation savedRelation = new DishRelation();
        savedRelation.setId("relation-2");
        savedRelation.setDishMetadata(dishMetadata);
        savedRelation.setIngredient(ingredient);

        DishRelationDTO payload = new DishRelationDTO();
        payload.setDishMetadataId(dishMetadata.getId());
        payload.setIngredientId(ingredient.getId());

        when(dishMetadataServiceImp.getEntityById(dishMetadata.getId()))
                .thenReturn(dishMetadata);

        when(ingredientServiceImp.getEntityById(ingredient.getId()))
                .thenReturn(ingredient);

        when(dishRelationRepository.save(any(DishRelation.class)))
                .thenReturn(savedRelation);

        DishRelationResponseDTO result = dishRelationServiceImp.create(payload);

        assertThat(result.getDishMetadata().getId()).isEqualTo(dishMetadata.getId());
        assertThat(result.getLabel()).isNull();
        assertThat(result.getIngredient().getId()).isEqualTo(ingredient.getId());

        verify(dishMetadataServiceImp).getEntityById(dishMetadata.getId());
        verify(ingredientServiceImp).getEntityById(ingredient.getId());
        verify(labelServiceImp, never()).getEntityById(any());
        verify(dishRelationRepository).save(any(DishRelation.class));
    }

    @Test
    void shouldCreate() {
        DishRelation savedRelation = new DishRelation();
        savedRelation.setId("relation-2");
        savedRelation.setDishMetadata(dishMetadata);
        savedRelation.setIngredient(ingredient);
        savedRelation.setLabel(label);

        DishRelationDTO payload = new DishRelationDTO();
        payload.setDishMetadataId(dishMetadata.getId());
        payload.setIngredientId(ingredient.getId());
        payload.setLabelId(label.getId());

        when(dishMetadataServiceImp.getEntityById(dishMetadata.getId()))
                .thenReturn(dishMetadata);

        when(ingredientServiceImp.getEntityById(ingredient.getId()))
                .thenReturn(ingredient);

        when(labelServiceImp.getEntityById(label.getId()))
                .thenReturn(label);

        when(dishRelationRepository.save(any(DishRelation.class)))
                .thenReturn(savedRelation);

        DishRelationResponseDTO result = dishRelationServiceImp.create(payload);

        assertThat(result.getDishMetadata().getId()).isEqualTo(dishMetadata.getId());
        assertThat(result.getIngredient().getId()).isEqualTo(ingredient.getId());
        assertThat(result.getLabel().getId()).isEqualTo(label.getId());

        verify(dishMetadataServiceImp).getEntityById(dishMetadata.getId());
        verify(ingredientServiceImp).getEntityById(ingredient.getId());
        verify(labelServiceImp).getEntityById(label.getId());
        verify(dishRelationRepository).save(any(DishRelation.class));
    }

    @Test
    void shouldGetById() {
        String id = relation.getId();

        when(dishRelationRepository.findById(id)).thenReturn(Optional.of(relation));

        DishRelationResponseDTO result = dishRelationServiceImp.getById(id);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getDishMetadata().getId()).isEqualTo(dishMetadata.getId());
        assertThat(result.getIngredient().getId()).isEqualTo(ingredient.getId());
        assertThat(result.getLabel().getId()).isEqualTo(label.getId());

        verify(dishRelationRepository).findById(id);
    }

    @Test
    void shouldThrowWithUnknownId() {
        String id = "non-existing-id";

        when(dishRelationRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dishRelationServiceImp.getById(id))
                .isInstanceOf(DishRelationNotFoundException.class)
                .hasMessageContaining(id);

        verify(dishRelationRepository).findById(id);
    }

    @Test
    void shouldUpdateById() {
        String id = relation.getId();
        DishRelationUpdateDTO payload = new DishRelationUpdateDTO();
        payload.setDishMetadataId(dishMetadata2.getId());
        payload.setIngredientId(ingredient2.getId());
        payload.setLabelId(label2.getId());

        when(dishRelationRepository.findById(id))
                .thenReturn(Optional.of(relation));

        when(dishMetadataServiceImp.getEntityById(dishMetadata2.getId()))
                .thenReturn(dishMetadata2);

        when(ingredientServiceImp.getEntityById(ingredient2.getId()))
                .thenReturn(ingredient2);

        when(labelServiceImp.getEntityById(label2.getId()))
                .thenReturn(label2);

        when(dishRelationRepository.save(any(DishRelation.class)))
                .thenReturn(relation);

        DishRelationResponseDTO result = dishRelationServiceImp.updateById(id, payload);

        assertThat(result.getDishMetadata().getId()).isEqualTo(dishMetadata2.getId());
        assertThat(result.getIngredient().getId()).isEqualTo(ingredient2.getId());
        assertThat(result.getLabel().getId()).isEqualTo(label2.getId());

        verify(dishMetadataServiceImp).getEntityById(dishMetadata2.getId());
        verify(ingredientServiceImp).getEntityById(ingredient2.getId());
        verify(labelServiceImp).getEntityById(label2.getId());
        verify(dishRelationRepository).save(any(DishRelation.class));
    }

    @Test
    void shouldUpdateByIdWithoutDishMetadata() {
        String id = relation.getId();
        DishRelationUpdateDTO payload = new DishRelationUpdateDTO();
        payload.setIngredientId(ingredient2.getId());
        payload.setLabelId(label2.getId());

        when(dishRelationRepository.findById(id))
                .thenReturn(Optional.of(relation));

        when(ingredientServiceImp.getEntityById(ingredient2.getId()))
                .thenReturn(ingredient2);

        when(labelServiceImp.getEntityById(label2.getId()))
                .thenReturn(label2);

        when(dishRelationRepository.save(any(DishRelation.class)))
                .thenReturn(relation);

        DishRelationResponseDTO result = dishRelationServiceImp.updateById(id, payload);

        assertThat(result.getDishMetadata().getId()).isEqualTo(dishMetadata.getId());
        assertThat(result.getIngredient().getId()).isEqualTo(ingredient2.getId());
        assertThat(result.getLabel().getId()).isEqualTo(label2.getId());

        verify(ingredientServiceImp).getEntityById(ingredient2.getId());
        verify(labelServiceImp).getEntityById(label2.getId());
        verify(dishRelationRepository).save(any(DishRelation.class));
        verifyNoInteractions(dishMetadataServiceImp);
    }

    @Test
    void shouldUpdateByIdWithoutIngredientId() {
        String id = relation.getId();
        DishRelationUpdateDTO payload = new DishRelationUpdateDTO();
        payload.setLabelId(label2.getId());

        when(dishRelationRepository.findById(id))
                .thenReturn(Optional.of(relation));

        when(labelServiceImp.getEntityById(label2.getId()))
                .thenReturn(label2);

        when(dishRelationRepository.save(any(DishRelation.class)))
                .thenReturn(relation);

        DishRelationResponseDTO result = dishRelationServiceImp.updateById(id, payload);

        assertThat(result.getDishMetadata().getId()).isEqualTo(dishMetadata.getId());
        assertThat(result.getIngredient().getId()).isEqualTo(ingredient.getId());
        assertThat(result.getLabel().getId()).isEqualTo(label2.getId());

        verify(labelServiceImp).getEntityById(label2.getId());
        verify(dishRelationRepository).save(any(DishRelation.class));
        verifyNoInteractions(dishMetadataServiceImp);
        verifyNoInteractions(ingredientServiceImp);
    }

    @Test
    void shouldUpdateByIdWithoutAnyPayload() {
        String id = relation.getId();
        DishRelationUpdateDTO payload = new DishRelationUpdateDTO();

        when(dishRelationRepository.findById(id))
                .thenReturn(Optional.of(relation));

        when(dishRelationRepository.save(any(DishRelation.class)))
                .thenReturn(relation);

        DishRelationResponseDTO result = dishRelationServiceImp.updateById(id, payload);

        assertThat(result.getDishMetadata().getId()).isEqualTo(dishMetadata.getId());
        assertThat(result.getIngredient().getId()).isEqualTo(ingredient.getId());
        assertThat(result.getLabel().getId()).isEqualTo(label.getId());

        verify(dishRelationRepository).save(any(DishRelation.class));
        verifyNoInteractions(dishMetadataServiceImp);
        verifyNoInteractions(ingredientServiceImp);
        verifyNoInteractions(labelServiceImp);
    }

    @Test
    void shouldDeleteById() {
        String id = relation.getId();

        when(dishRelationRepository.findById(id))
                .thenReturn(Optional.of(relation));

        dishRelationServiceImp.deleteById(id);

        verify(dishRelationRepository).findById(id);
        verify(dishRelationRepository).delete(relation);
    }
}