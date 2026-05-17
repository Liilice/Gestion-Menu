package com.example.backoffice.Service.Integration;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.backoffice.DTO.label.LabelDTO;
import com.example.backoffice.DTO.label.LabelResponseDTO;
import com.example.backoffice.Entity.Label;
import com.example.backoffice.Exception.notFoundException.LabelNotFoundException;
import com.example.backoffice.Repository.LabelRepository;
import com.example.backoffice.Service.LabelServiceImp;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class LabelServiceIntegrationTests {
    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelServiceImp labelServiceImp;

    private Label label1;
    private Label label2;
    private String unknownId;

    @BeforeEach
    void setup() {
        unknownId = "non-existing-id";

        labelRepository.deleteAll();

        label1 = createLabel("vegan");
        label2 = createLabel("spicy");
    }

    @Test
    void shouldGetAll() {
        List<LabelResponseDTO> result = labelServiceImp.getAll();

        assertThat(result)
                .hasSize(2)
                .extracting(LabelResponseDTO::getName)
                .containsExactlyInAnyOrder(
                        label1.getName(),
                        label2.getName()
                );
    }

    @Test
    void shouldCreate() {
        LabelDTO payload = new LabelDTO();
        payload.setName("Gluten Free");

        LabelResponseDTO result = labelServiceImp.create(payload);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("gluten free");

        Label saved = labelRepository.findById(result.getId()).orElseThrow();

        assertThat(saved.getName()).isEqualTo("gluten free");
    }

    @Test
    void shouldGetById() {
        LabelResponseDTO result = labelServiceImp.getById(label1.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(label1.getId());
        assertThat(result.getName()).isEqualTo(label1.getName());
    }

    @Test
    void shouldThrowWhenGetByIdUnknown() {
        assertThat(labelRepository.findById(unknownId)).isEmpty();

        assertThatThrownBy(() -> labelServiceImp.getById(unknownId))
                .isInstanceOf(LabelNotFoundException.class)
                .hasMessageContaining(unknownId);
    }

    @Test
    void shouldUpdateById() {
        LabelDTO payload = new LabelDTO();
        payload.setName("Signature");

        LabelResponseDTO result = labelServiceImp.updateById(label1.getId(), payload);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(label1.getId());
        assertThat(result.getName()).isEqualTo("signature");

        Label saved = labelRepository.findById(label1.getId()).orElseThrow();

        assertThat(saved.getName()).isEqualTo("signature");
    }

    @Test
    void shouldThrowWhenUpdateUnknownId() {
        LabelDTO payload = new LabelDTO();
        payload.setName("test");

        assertThatThrownBy(() -> labelServiceImp.updateById(unknownId, payload))
                .isInstanceOf(LabelNotFoundException.class)
                .hasMessageContaining(unknownId);
    }

    @Test
    void shouldDeleteById() {
        String id = label1.getId();

        labelServiceImp.deleteById(id);

        assertThat(labelRepository.existsById(id)).isFalse();
    }

    @Test
    void shouldThrowWhenDeleteUnknownId() {
        assertThatThrownBy(() -> labelServiceImp.deleteById(unknownId))
                .isInstanceOf(LabelNotFoundException.class)
                .hasMessageContaining(unknownId);
    }

    private Label createLabel(String name) {
        Label label = new Label();
        label.setName(name);
        return labelRepository.save(label);
    }
}