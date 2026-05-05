package com.example.backoffice.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.containsInAnyOrder;


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

import com.example.backoffice.DTO.dishMetadata.DishMetadataDTO;
import com.example.backoffice.DTO.dishMetadata.DishMetadataForGetResponseDTO;
import com.example.backoffice.DTO.dishMetadata.DishMetadataResponseDTO;
import com.example.backoffice.DTO.dishMetadata.DishMetadataUpdateDTO;
import com.example.backoffice.Exception.notFoundException.DishMetadataNotFoundException;
import com.example.backoffice.Service.DishMetadataService;

import tools.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DishMetadataController.class)
public class DishMetadataControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private DishMetadataService dishMetadataService;

    private DishMetadataForGetResponseDTO res1;
    private DishMetadataForGetResponseDTO res2;

    private DishMetadataForGetResponseDTO createDishMetadata(DishMetadataDTO payload) {
        DishMetadataForGetResponseDTO dishMetadata = new DishMetadataForGetResponseDTO();
        dishMetadata.setId("id-" + payload.getName().toLowerCase());
        dishMetadata.setName(payload.getName());
        dishMetadata.setDescription(payload.getDescription());
        dishMetadata.setPrice(payload.getPrice());
        return dishMetadata;
    }

    private DishMetadataDTO createPayload(String name, String description, BigDecimal price) {
        DishMetadataDTO dto = new DishMetadataDTO();
        dto.setName(name);
        dto.setDescription(description);
        dto.setPrice(price);
        return dto;
    }

    @BeforeEach
    void setup() {
        res1 = createDishMetadata(
                createPayload("bibimpap", "plat coréen", BigDecimal.valueOf(12.50)));

        res2 = createDishMetadata(
                createPayload("bao", "plat chinois", BigDecimal.valueOf(8.00)));

    }

    @Test
    public void shouldGetAll() throws Exception {
        when(dishMetadataService.getAll()).thenReturn(List.of(res1, res2));

        mockMvc.perform(get("/dishMetadata")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(res1.getId()))
                .andExpect(jsonPath("$[0].name").value(res1.getName()))
                .andExpect(jsonPath("$[1].id").value(res2.getId()))
                .andExpect(jsonPath("$[1].name").value(res2.getName()));

        verify(dishMetadataService).getAll();
    }

    @Test
    public void shouldCreate() throws Exception {
        DishMetadataDTO payload = new DishMetadataDTO();
        payload.setName("canard laqué");
        payload.setDescription("style pékin");
        payload.setPrice(BigDecimal.valueOf(20.00));

        DishMetadataResponseDTO res = new DishMetadataResponseDTO();
        res.setId("id-canard");
        res.setName("canard laqué");
        res.setDescription("style pékin");
        res.setPrice(BigDecimal.valueOf(20.00));

        when(dishMetadataService.create(any(DishMetadataDTO.class)))
                .thenReturn(res);

        mockMvc.perform(post("/dishMetadata")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(res.getId()))
                .andExpect(jsonPath("$.name").value(payload.getName()))
                .andExpect(jsonPath("$.description").value(payload.getDescription()))
                .andExpect(jsonPath("$.price").value(payload.getPrice()));

        ArgumentCaptor<DishMetadataDTO> captor = ArgumentCaptor.forClass(DishMetadataDTO.class);

        verify(dishMetadataService).create(captor.capture());

        DishMetadataDTO captured = captor.getValue();

        assertThat(captured.getName()).isEqualTo(payload.getName());
        assertThat(captured.getDescription()).isEqualTo(payload.getDescription());
        assertThat(captured.getPrice()).isEqualByComparingTo(payload.getPrice());
    }

    @Test
    public void shouldCreateWithInvalidPayload() throws Exception {
        DishMetadataDTO payload = new DishMetadataDTO();
        payload.setDescription("style pékin");

        mockMvc.perform(post("/dishMetadata")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors")
                        .isArray())
                .andExpect(jsonPath("$.errors")
                        .value(containsInAnyOrder(
                                "name: Name field is required", "price: Price field is required")));
        verifyNoInteractions(dishMetadataService);
    }

    @Test
    public void shouldCreateWithNegativePrice() throws Exception {
        DishMetadataDTO payload = new DishMetadataDTO();
        payload.setName("canard laqué");
        payload.setDescription("style pékin");
        payload.setPrice(BigDecimal.valueOf(-10));

        mockMvc.perform(post("/dishMetadata")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors")
                        .isArray())
                .andExpect(jsonPath("$.errors")
                        .value(containsInAnyOrder(
                                "price: Price must be a positive value")));
        verifyNoInteractions(dishMetadataService);
    }

    @Test
    public void shouldGetById() throws Exception {
        String id = res1.getId();
        when(dishMetadataService.getById(id)).thenReturn(res1);

        mockMvc.perform(get("/dishMetadata/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(res1.getName()))
                .andExpect(jsonPath("$.description").value(res1.getDescription()))
                .andExpect(jsonPath("$.price").value(res1.getPrice()));

        verify(dishMetadataService).getById(id);
    }

    @Test
    public void shouldGetByIdNotFound() throws Exception {
        String id = "non-existing-id";
        when(dishMetadataService.getById(id)).thenThrow(new DishMetadataNotFoundException(id));

        mockMvc.perform(get("/dishMetadata/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("DishMetadata not found with id: " + id));

        verify(dishMetadataService).getById(id);
    }

    @Test
    public void shouldUpdateById() throws Exception {
        String id = res1.getId();

        DishMetadataUpdateDTO payload = new DishMetadataUpdateDTO();
        payload.setDescription("style pékin");
        payload.setPrice(BigDecimal.valueOf(10));

        DishMetadataResponseDTO updatedRes = new DishMetadataResponseDTO();
        updatedRes.setId(id);
        updatedRes.setName(res1.getName());
        updatedRes.setDescription("style pékin");
        updatedRes.setPrice(BigDecimal.valueOf(10));

        when(dishMetadataService.updateById(eq(id), any(DishMetadataUpdateDTO.class)))
                .thenReturn(updatedRes);

        mockMvc.perform(put("/dishMetadata/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(res1.getName()))
                .andExpect(jsonPath("$.description").value(payload.getDescription()))
                .andExpect(jsonPath("$.price").value(payload.getPrice()));

        ArgumentCaptor<DishMetadataUpdateDTO> captor = ArgumentCaptor.forClass(DishMetadataUpdateDTO.class);

        verify(dishMetadataService).updateById(eq(id), captor.capture());

        DishMetadataUpdateDTO captured = captor.getValue();

        assertThat(captured.getName()).isNull();
        assertThat(captured.getDescription()).isEqualTo(payload.getDescription());
        assertThat(captured.getPrice()).isEqualByComparingTo(payload.getPrice());
    }

    @Test
    public void shouldDeleteById() throws Exception {
        String id = res1.getId();

        mockMvc.perform(delete("/dishMetadata/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(dishMetadataService).deleteById(id);
    }

}
