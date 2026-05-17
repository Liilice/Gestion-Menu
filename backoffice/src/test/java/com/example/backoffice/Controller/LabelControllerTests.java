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

import com.example.backoffice.DTO.label.LabelDTO;
import com.example.backoffice.DTO.label.LabelResponseDTO;
import com.example.backoffice.Exception.notFoundException.LabelNotFoundException;
import com.example.backoffice.Service.LabelServiceImp;

import tools.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = LabelController.class)
class LabelControllerTests {
        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;
        @MockitoBean
        private LabelServiceImp labelService;

        private LabelResponseDTO res1;
        private LabelResponseDTO res2;

        @BeforeEach
        void setup() {
                res1 = new LabelResponseDTO();
                res1.setId("id-crustacés");
                res1.setName("crustacés");

                res2 = new LabelResponseDTO();
                res2.setId("id-arachides");
                res2.setName("arachides");
        }

        @Test
        void shouldGetAll() throws Exception {
                when(labelService.getAll()).thenReturn(List.of(res1, res2));

                mockMvc.perform(get("/label")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].id").value(res1.getId()))
                                .andExpect(jsonPath("$[0].name").value(res1.getName()))
                                .andExpect(jsonPath("$[1].id").value(res2.getId()))
                                .andExpect(jsonPath("$[1].name").value(res2.getName()));

                verify(labelService).getAll();
        }

        @Test
        void shouldGetEmptyList() throws Exception {
                when(labelService.getAll()).thenReturn(List.of());

                mockMvc.perform(get("/label")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(0));

                verify(labelService).getAll();
        }

        @Test
        void shouldCreate() throws Exception {
                LabelDTO payload = new LabelDTO();
                payload.setName("lupin");

                LabelResponseDTO res = new LabelResponseDTO();
                res.setId("id-lupin");
                res.setName("lupin");

                when(labelService.create(any(LabelDTO.class)))
                                .thenReturn(res);

                mockMvc.perform(post("/label")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(payload)))
                                .andExpect(status().isCreated())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id").value("id-lupin"))
                                .andExpect(jsonPath("$.name").value("lupin"));

                verify(labelService).create(argThat(dto -> dto.getName().equals("lupin")));
        }

        @Test
        void shouldCreateWithInvalidPayload() throws Exception {
                LabelDTO payload = new LabelDTO();
                payload.setName("");

                mockMvc.perform(post("/label")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(payload)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.success").value(false))
                                .andExpect(jsonPath("$.message").value("Validation failed"))
                                .andExpect(jsonPath("$.errors[0]").value("name: Name field is required"));

                verifyNoInteractions(labelService);
        }

        @Test
        void shouldGetById() throws Exception {
                String id = res1.getId();
                when(labelService.getById(id)).thenReturn(res1);

                mockMvc.perform(get("/label/{id}", id)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id").value(id))
                                .andExpect(jsonPath("$.name").value(res1.getName()));
                verify(labelService).getById(id);
        }

        @Test
        void shouldGetByIdNotFound() throws Exception {
                String id = "non-existing-id";
                when(labelService.getById(id)).thenThrow(new LabelNotFoundException(id));

                mockMvc.perform(get("/label/{id}", id)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.success").value(false))
                                .andExpect(jsonPath("$.message").value("Label not found with id: non-existing-id"));

                verify(labelService).getById(id);
        }

        @Test
        void shouldUpdateById() throws Exception {
                String id = res1.getId();

                LabelDTO payload = new LabelDTO();
                payload.setName("crustacés frais");

                LabelResponseDTO updatedRes = new LabelResponseDTO();
                updatedRes.setId(id);
                updatedRes.setName("crustacés frais");

                when(labelService.updateById(eq(id), any(LabelDTO.class)))
                                .thenReturn(updatedRes);

                mockMvc.perform(put("/label/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(payload)))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id").value(id))
                                .andExpect(jsonPath("$.name").value("crustacés frais"));

                verify(labelService).updateById(argThat(s -> s.equals(id)),
                                argThat(dto -> dto.getName().equals("crustacés frais")));
        }

        @Test
        void shouldUpdateWithInvalidPayload() throws Exception {
                String id = res1.getId();
                LabelDTO payload = new LabelDTO();
                payload.setName("");

                mockMvc.perform(put("/label/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(payload)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.success").value(false))
                                .andExpect(jsonPath("$.message").value("Validation failed"))
                                .andExpect(jsonPath("$.errors[0]").value("name: Name field is required"));

                verifyNoInteractions(labelService);
        }

        @Test
        void shouldUpdateByIdNotFound() throws Exception {
                String id = "non-existing-id";
                LabelDTO payload = new LabelDTO();
                payload.setName("test");
                when(labelService.updateById(eq(id), any(LabelDTO.class))).thenThrow(new LabelNotFoundException(id));

                mockMvc.perform(put("/label/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(payload)))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.success").value(false))
                                .andExpect(jsonPath("$.message").value("Label not found with id: non-existing-id"));

                verify(labelService).updateById(argThat(s -> s.equals(id)), any(LabelDTO.class));
        }

        @Test
        void shouldDeleteById() throws Exception {
                String id = res1.getId();

                mockMvc.perform(delete("/label/{id}", id))
                                .andExpect(status().isNoContent());

                verify(labelService).deleteById(id);
        }

        @Test
        void shouldDeleteByIdNotFound() throws Exception {
                String id = "non-existing-id";

                doThrow(new LabelNotFoundException(id))
                                .when(labelService)
                                .deleteById(id);

                mockMvc.perform(delete("/label/{id}", id))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.success").value(false))
                                .andExpect(jsonPath("$.message").value("Label not found with id: " + id));

                verify(labelService).deleteById(id);
        }
}
