package com.example.backoffice.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.backoffice.DTO.category.CategoryDTO;
import com.example.backoffice.DTO.category.CategoryResponseDTO;
import com.example.backoffice.Exception.notFoundException.CategoryNotFoundException;
import com.example.backoffice.Service.CategoryService;

import tools.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CategoryController.class)
public class CategoryControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryService categoryService;

    private CategoryResponseDTO res1;
    private CategoryResponseDTO res2;

    @BeforeEach
    void setup() {
        res1 = new CategoryResponseDTO();
        res1.setId("id-légumes");
        res1.setName("légumes");

        res2 = new CategoryResponseDTO();
        res2.setId("id-protéines");
        res2.setName("protéines");
    }

    @Test
    public void shouldGetAll() throws Exception {
        when(categoryService.getAll()).thenReturn(List.of(res1, res2));

        mockMvc.perform(get("/categories")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(res1.getId()))
                .andExpect(jsonPath("$[0].name").value("légumes"))
                .andExpect(jsonPath("$[1].id").value(res2.getId()))
                .andExpect(jsonPath("$[1].name").value("protéines"));

        verify(categoryService).getAll();
    }

    @Test
    public void shouldGetEmptyList() throws Exception {
        when(categoryService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/categories")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(categoryService).getAll();
    }

    @Test
    public void shouldCreate() throws Exception {
        CategoryDTO payload = new CategoryDTO();
        payload.setName("fruit");

        CategoryResponseDTO res = new CategoryResponseDTO();
        res.setId("id-fruit");
        res.setName("fruit");

        when(categoryService.create(any(CategoryDTO.class)))
                .thenReturn(res);

        mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("id-fruit"))
                .andExpect(jsonPath("$.name").value("fruit"));

        verify(categoryService).create(argThat(dto -> dto.getName().equals("fruit")));
    }

    @Test
    public void shouldCreateWithInvalidPayload() throws Exception {
        CategoryDTO payload = new CategoryDTO();
        payload.setName("");

        mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors[0]").value("name: Name field is required"));

        verifyNoInteractions(categoryService);
    }

    @Test
    public void shouldGetById() throws Exception {
        String id = res1.getId();
        when(categoryService.getById(id)).thenReturn(res1);

        mockMvc.perform(get("/categories/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("légumes"));
        verify(categoryService).getById(id);
    }

    @Test
    public void shouldGetByIdNotFound() throws Exception {
        String id = "non-existing-id";
        when(categoryService.getById(id)).thenThrow(new CategoryNotFoundException(id));

        mockMvc.perform(get("/categories/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Category not found with id: non-existing-id"));

        verify(categoryService).getById(id);
    }

    @Test
    public void shouldUpdateById() throws Exception {
        String id = res1.getId();

        CategoryDTO payload = new CategoryDTO();
        payload.setName("légumes frais");

        CategoryResponseDTO updatedRes = new CategoryResponseDTO();
        updatedRes.setId(id);
        updatedRes.setName("légumes frais");

        when(categoryService.updateById(eq(id), any(CategoryDTO.class)))
                .thenReturn(updatedRes);

        mockMvc.perform(put("/categories/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("légumes frais"));

        verify(categoryService).updateById(argThat(s -> s.equals(id)),
                argThat(dto -> dto.getName().equals("légumes frais")));
    }

    @Test
    public void shouldUpdateWithInvalidPayload() throws Exception {
        String id = res1.getId();
        CategoryDTO payload = new CategoryDTO();
        payload.setName("");

        mockMvc.perform(put("/categories/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors[0]").value("name: Name field is required"));

        verifyNoInteractions(categoryService);
    }

    @Test
    public void shouldUpdateByIdNotFound() throws Exception {
        String id = "non-existing-id";
        CategoryDTO payload = new CategoryDTO();
        payload.setName("test");
        when(categoryService.updateById(eq(id), any(CategoryDTO.class))).thenThrow(new CategoryNotFoundException(id));

        mockMvc.perform(put("/categories/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Category not found with id: non-existing-id"));

        verify(categoryService).updateById(argThat(s -> s.equals(id)), any(CategoryDTO.class));
    }

    @Test
    public void shouldDeleteById() throws Exception {
        String id = res1.getId();

        mockMvc.perform(delete("/categories/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(categoryService).deleteById(id);
    }

    @Test
    public void shouldDeleteByIdNotFound() throws Exception {
        String id = "non-existing-id";

        doThrow(new CategoryNotFoundException(id))
                .when(categoryService)
                .deleteById(id);

        mockMvc.perform(delete("/categories/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Category not found with id: " + id));

        verify(categoryService).deleteById(id);
    }
}
