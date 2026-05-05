package com.example.backoffice.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.backoffice.DTO.category.CategoryWithoutIngredientDTO;
import com.example.backoffice.DTO.dishMetadata.DishMetadataResponseDTO;
import com.example.backoffice.DTO.dishRelation.DishRelationDTO;
import com.example.backoffice.DTO.dishRelation.DishRelationResponseDTO;
import com.example.backoffice.DTO.dishRelation.DishRelationUpdateDTO;
import com.example.backoffice.DTO.ingredient.IngredientWithCategoryDTO;
import com.example.backoffice.DTO.label.LabelResponseDTO;
import com.example.backoffice.Exception.notFoundException.DishRelationNotFoundException;
import com.example.backoffice.Service.DishRelationService;

import tools.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.containsInAnyOrder;

@WebMvcTest(controllers = DishRelationController.class)
public class DishRelationControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private DishRelationService dishRelationService;

    private DishRelationResponseDTO res1;
    private DishMetadataResponseDTO dishMetadata;
    private IngredientWithCategoryDTO ingredient;
    private LabelResponseDTO label;

    @BeforeEach
    void setup() {
        dishMetadata = new DishMetadataResponseDTO();
        dishMetadata.setId("dish-1");
        dishMetadata.setName("bibimpap");
        dishMetadata.setDescription("plat coréen");
        dishMetadata.setPrice(BigDecimal.valueOf(12.50));

        CategoryWithoutIngredientDTO category = new CategoryWithoutIngredientDTO();
        category.setId("légumes");
        category.setName("légumes");

        ingredient = new IngredientWithCategoryDTO();
        ingredient.setId("ingredient-1");
        ingredient.setName("brocoli");
        ingredient.setCategory(category);

        label = new LabelResponseDTO();
        label.setId("label-1");
        label.setName("spicy");

        res1 = new DishRelationResponseDTO();
        res1.setId("relation-1");
        res1.setDishMetadata(dishMetadata);
        res1.setIngredient(ingredient);
        res1.setLabel(label);
    }

    @Test
    public void shouldGetAll() throws Exception {
        when(dishRelationService.getAll()).thenReturn(List.of(res1));

        mockMvc.perform(get("/dishRelation")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].dishMetadata.id").value(dishMetadata.getId()))
                .andExpect(jsonPath("$[0].dishMetadata.name").value(dishMetadata.getName()))
                .andExpect(jsonPath("$[0].dishMetadata.description").value(dishMetadata.getDescription()))
                .andExpect(jsonPath("$[0].dishMetadata.price").value(dishMetadata.getPrice()))
                .andExpect(jsonPath("$[0].ingredient.id").value(ingredient.getId()))
                .andExpect(jsonPath("$[0].ingredient.name").value(ingredient.getName()))
                .andExpect(jsonPath("$[0].ingredient.category.id").value(ingredient.getCategory().getId()))
                .andExpect(jsonPath("$[0].ingredient.category.name").value(ingredient.getCategory().getName()))
                .andExpect(jsonPath("$[0].label.id").value(label.getId()))
                .andExpect(jsonPath("$[0].label.name").value(label.getName()));

        verify(dishRelationService).getAll();
    }

    @Test
    public void shouldCreate() throws Exception {
        DishRelationDTO payload = new DishRelationDTO();
        payload.setDishMetadataId(dishMetadata.getId());
        payload.setIngredientId(ingredient.getId());
        payload.setLabelId(label.getId());

        DishRelationResponseDTO res = new DishRelationResponseDTO();
        res.setId("relation-2");
        res.setDishMetadata(dishMetadata);
        res.setIngredient(ingredient);
        res.setLabel(label);

        when(dishRelationService.create(any(DishRelationDTO.class)))
                .thenReturn(res);

        mockMvc.perform(post("/dishRelation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(res.getId()))
                .andExpect(jsonPath("$.dishMetadata.id").value(dishMetadata.getId()))
                .andExpect(jsonPath("$.dishMetadata.name").value(dishMetadata.getName()))
                .andExpect(jsonPath("$.dishMetadata.description").value(dishMetadata.getDescription()))
                .andExpect(jsonPath("$.dishMetadata.price").value(dishMetadata.getPrice()))
                .andExpect(jsonPath("$.ingredient.id").value(ingredient.getId()))
                .andExpect(jsonPath("$.ingredient.name").value(ingredient.getName()))
                .andExpect(jsonPath("$.ingredient.category.id").value(ingredient.getCategory().getId()))
                .andExpect(jsonPath("$.ingredient.category.name").value(ingredient.getCategory().getName()))
                .andExpect(jsonPath("$.label.id").value(label.getId()))
                .andExpect(jsonPath("$.label.name").value(label.getName()));

        ArgumentCaptor<DishRelationDTO> captor = ArgumentCaptor.forClass(DishRelationDTO.class);

        verify(dishRelationService).create(captor.capture());

        DishRelationDTO captured = captor.getValue();

        assertThat(captured.getDishMetadataId()).isEqualTo(res.getDishMetadata().getId());
        assertThat(captured.getIngredientId()).isEqualTo(res.getIngredient().getId());
        assertThat(captured.getLabelId()).isEqualTo(res.getLabel().getId());
    }

    @Test
    public void shouldCreateWithInvalidPayload() throws Exception {
        DishRelationDTO payload = new DishRelationDTO();
        payload.setIngredientId(ingredient.getId());
        payload.setLabelId(label.getId());

        mockMvc.perform(post("/dishRelation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors")
                        .isArray())
                .andExpect(jsonPath("$.errors")
                        .value(containsInAnyOrder(
                                "dishMetadataId: DishMetadataId is required")));

        verifyNoInteractions(dishRelationService);
    }

    @Test
    public void shouldGetById() throws Exception {
        String id = res1.getId();
        when(dishRelationService.getById(id)).thenReturn(res1);

        mockMvc.perform(get("/dishRelation/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.dishMetadata.id").value(dishMetadata.getId()))
                .andExpect(jsonPath("$.dishMetadata.name").value(dishMetadata.getName()))
                .andExpect(jsonPath("$.dishMetadata.description").value(dishMetadata.getDescription()))
                .andExpect(jsonPath("$.dishMetadata.price").value(dishMetadata.getPrice()))
                .andExpect(jsonPath("$.ingredient.id").value(ingredient.getId()))
                .andExpect(jsonPath("$.ingredient.name").value(ingredient.getName()))
                .andExpect(jsonPath("$.ingredient.category.id").value(ingredient.getCategory().getId()))
                .andExpect(jsonPath("$.ingredient.category.name").value(ingredient.getCategory().getName()))
                .andExpect(jsonPath("$.label.id").value(label.getId()))
                .andExpect(jsonPath("$.label.name").value(label.getName()));

        verify(dishRelationService).getById(id);
    }

    @Test
    public void shouldGetByIdNotFound() throws Exception {
        String id = "non-existing-id";
        when(dishRelationService.getById(id)).thenThrow(new DishRelationNotFoundException(id));

        mockMvc.perform(get("/dishRelation/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("DishRelation not found with id: " + id));

        verify(dishRelationService).getById(id);
    }

    @Test
    public void shouldUpdateById() throws Exception {
        String id = res1.getId();

        DishRelationUpdateDTO payload = new DishRelationUpdateDTO();
        payload.setDishMetadataId(dishMetadata.getId());
        payload.setLabelId(label.getId());

        DishRelationResponseDTO updatedRes = new DishRelationResponseDTO();
        updatedRes.setId(id);
        updatedRes.setDishMetadata(dishMetadata);
        updatedRes.setIngredient(ingredient);
        updatedRes.setLabel(label);

        when(dishRelationService.updateById(eq(id), any(DishRelationUpdateDTO.class)))
                .thenReturn(updatedRes);

        mockMvc.perform(put("/dishRelation/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.dishMetadata.id").value(dishMetadata.getId()))
                .andExpect(jsonPath("$.dishMetadata.name").value(dishMetadata.getName()))
                .andExpect(jsonPath("$.dishMetadata.description").value(dishMetadata.getDescription()))
                .andExpect(jsonPath("$.dishMetadata.price").value(dishMetadata.getPrice()))
                .andExpect(jsonPath("$.ingredient.id").value(ingredient.getId()))
                .andExpect(jsonPath("$.ingredient.name").value(ingredient.getName()))
                .andExpect(jsonPath("$.ingredient.category.id").value(ingredient.getCategory().getId()))
                .andExpect(jsonPath("$.ingredient.category.name").value(ingredient.getCategory().getName()))
                .andExpect(jsonPath("$.label.id").value(label.getId()))
                .andExpect(jsonPath("$.label.name").value(label.getName()));

        ArgumentCaptor<DishRelationUpdateDTO> captor = ArgumentCaptor.forClass(DishRelationUpdateDTO.class);

        verify(dishRelationService).updateById(eq(id), captor.capture());

        DishRelationUpdateDTO captured = captor.getValue();

        assertThat(captured.getDishMetadataId()).isEqualTo(payload.getDishMetadataId());
        assertThat(captured.getIngredientId()).isNull();
        assertThat(captured.getLabelId()).isEqualTo(payload.getLabelId());
    }

    @Test
    public void shouldDeleteById() throws Exception {
        String id = res1.getId();

        mockMvc.perform(delete("/dishRelation/{id}", id))
                .andExpect(status().isNoContent());

        verify(dishRelationService).deleteById(id);
    }

}
