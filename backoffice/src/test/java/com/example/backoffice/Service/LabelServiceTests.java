package com.example.backoffice.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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

import com.example.backoffice.DTO.label.LabelDTO;
import com.example.backoffice.DTO.label.LabelResponseDTO;
import com.example.backoffice.Entity.Label;
import com.example.backoffice.Exception.notFoundException.LabelNotFoundException;
import com.example.backoffice.Mapper.LabelMapper;
import com.example.backoffice.Repository.LabelRepository;

@ExtendWith(MockitoExtension.class)
public class LabelServiceTests {
    @Mock
    private LabelRepository labelRepository;

    private LabelMapper labelMapper;

    private LabelServiceImp labelServiceImp;

    private Label label;
    private Label label2;

    @BeforeEach
    void setup() {
        labelMapper = Mappers.getMapper(LabelMapper.class);

        labelServiceImp = new LabelServiceImp();

        ReflectionTestUtils.setField(labelServiceImp, "labelRepository", labelRepository);
        ReflectionTestUtils.setField(labelServiceImp, "labelMapper", labelMapper);

        label = new Label();
        label.setId("label-1");
        label.setName("spicy");

        label2 = new Label();
        label2.setId("label-2");
        label2.setName("vegan");
    }

    @Test
    void shouldGetAll() {
        when(labelRepository.findAll()).thenReturn(List.of(label, label2));

        List<LabelResponseDTO> result = labelServiceImp.getAll();

        assertThat(result).hasSize(2)
                .extracting(LabelResponseDTO::getName)
                .containsExactly(label.getName(), label2.getName());

        verify(labelRepository).findAll();
    }

    @Test
    void shouldCreate() {
        Label lupin = new Label();
        lupin.setId("label-3");
        lupin.setName("lupin");

        LabelDTO payload = new LabelDTO();
        payload.setName("lupin");

        when(labelRepository.save(any(Label.class))).thenReturn(lupin);

        LabelResponseDTO result = labelServiceImp.create(payload);

        assertThat(result.getId()).isEqualTo(lupin.getId());
        assertThat(result.getName()).isEqualTo(payload.getName().toLowerCase());

        verify(labelRepository).save(any(Label.class));
    }

    @Test
    public void shouldGetById() {
        String id = label.getId();

        when(labelRepository.findById(id)).thenReturn(Optional.of(label));

        LabelResponseDTO result = labelServiceImp.getById(id);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo(label.getName());

        verify(labelRepository).findById(id);
    }

    @Test
    public void shouldThrowWithUnknownId() {
        String id = "non-existing-id";

        when(labelRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> labelServiceImp.getById(id))
                .isInstanceOf(LabelNotFoundException.class)
                .hasMessageContaining(id);

        verify(labelRepository).findById(id);
    }

    @Test
    public void shouldUpdateById() {
        String id = label.getId();

        when(labelRepository.findById(id))
                .thenReturn(Optional.of(label));

        LabelDTO payload = new LabelDTO();
        payload.setName("not spicy");

        when(labelRepository.save(any()))
                .thenReturn(label);

        labelServiceImp.updateById(id, payload);

        assertThat(label.getName()).isEqualTo(payload.getName().toLowerCase());

        verify(labelRepository).save(any());
    }

    @Test
    public void shouldDeleteById() {
        String id = label.getId();

        when(labelRepository.findById(id))
                .thenReturn(Optional.of(label));

        labelServiceImp.deleteById(id);

        verify(labelRepository).findById(id);
        verify(labelRepository).delete(label);
    }

}
