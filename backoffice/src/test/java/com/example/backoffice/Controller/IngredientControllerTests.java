package com.example.backoffice.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.backoffice.DTO.ingredient.IngredientDTO;
import com.example.backoffice.DTO.ingredient.IngredientResponseDTO;
import com.example.backoffice.DTO.ingredient.IngredientUpdateDTO;
import com.example.backoffice.Entity.Category;
import com.example.backoffice.Exception.notFoundException.IngredientNotFoundException;
import com.example.backoffice.Service.IngredientService;

import tools.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = IngredientController.class)
public class IngredientControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private IngredientService ingredientService;

    private IngredientResponseDTO res1;
    private Category category;

    @BeforeEach
    void setup() {
        category = new Category();
        category.setId("légumes");
        category.setName("légumes");

        res1 = new IngredientResponseDTO();
        res1.setId("ingredient-1");
        res1.setName("brocoli");
        res1.setCategoryId(category.getId());
        res1.setCategoryName(category.getName());
    }

    @Test
    public void shouldGetAll() throws Exception {
        when(ingredientService.getAll()).thenReturn(List.of(res1));

        mockMvc.perform(get("/ingredients")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(res1.getId()))
                .andExpect(jsonPath("$[0].name").value(res1.getName()))
                .andExpect(jsonPath("$[0].categoryId").value(res1.getCategoryId()))
                .andExpect(jsonPath("$[0].categoryName").value(res1.getCategoryName()));

        verify(ingredientService).getAll();
    }

    @Test
    public void shouldCreate() throws Exception {
        IngredientDTO payload = new IngredientDTO();
        payload.setName("chou");

        IngredientResponseDTO res = new IngredientResponseDTO();
        res.setId("id-chou");
        res.setName("chou");
        res.setCategoryId(category.getId());
        res.setCategoryName(category.getName());

        when(ingredientService.create(any(IngredientDTO.class)))
                .thenReturn(res);

        mockMvc.perform(post("/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("id-chou"))
                .andExpect(jsonPath("$.name").value("chou"));

        verify(ingredientService).create(argThat(dto -> dto.getName().equals("chou")));
    }

    @Test
    public void shouldCreateWithInvalidPayload() throws Exception {
        IngredientDTO payload = new IngredientDTO();
        payload.setName("");

        mockMvc.perform(post("/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors")
                        .value(containsInAnyOrder("name: Name is required")));

        verifyNoInteractions(ingredientService);
    }

    @Test
    public void shouldGetById() throws Exception {
        String id = res1.getId();
        when(ingredientService.getById(id)).thenReturn(res1);

        mockMvc.perform(get("/ingredients/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(res1.getName()))
                .andExpect(jsonPath("$.categoryId").value(res1.getCategoryId()))
                .andExpect(jsonPath("$.categoryName").value(res1.getCategoryName()));

        verify(ingredientService).getById(id);
    }

    @Test
    public void shouldGetByIdNotFound() throws Exception {
        String id = "non-existing-id";
        when(ingredientService.getById(id)).thenThrow(new IngredientNotFoundException(id));

        mockMvc.perform(get("/ingredients/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Ingredient not found with id: " + id));

        verify(ingredientService).getById(id);
    }

    @Test
    public void shouldUpdateById() throws Exception {
        String id = res1.getId();

        IngredientUpdateDTO payload = new IngredientUpdateDTO();
        payload.setName("chou fleur");

        IngredientResponseDTO updatedRes = new IngredientResponseDTO();
        updatedRes.setId(id);
        updatedRes.setName("chou fleur");

        when(ingredientService.updateById(eq(id), any(IngredientUpdateDTO.class)))
                .thenReturn(updatedRes);

        mockMvc.perform(put("/ingredients/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(payload.getName()))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(payload.getName()));

        ArgumentCaptor<IngredientUpdateDTO> captor = ArgumentCaptor.forClass(IngredientUpdateDTO.class);

        verify(ingredientService).updateById(eq(id), captor.capture());

        IngredientUpdateDTO captured = captor.getValue();

        assertThat(captured.getName()).isEqualTo(payload.getName());
        assertThat(captured.getCategoryId()).isNull();
    }

    @Test
    public void shouldDeleteById() throws Exception {
        String id = res1.getId();

        mockMvc.perform(delete("/ingredients/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(ingredientService).deleteById(id);
    }
}
