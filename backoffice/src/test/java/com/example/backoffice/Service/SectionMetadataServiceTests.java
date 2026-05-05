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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.backoffice.DTO.sectionMetadata.SectionMetadataDTO;
import com.example.backoffice.DTO.sectionMetadata.SectionMetadataForGetResponseDTO;
import com.example.backoffice.DTO.sectionMetadata.SectionMetadataResponseDTO;
import com.example.backoffice.Entity.SectionMetadata;
import com.example.backoffice.Exception.notFoundException.SectionMetadataNotFoundException;
import com.example.backoffice.Mapper.SectionMetadataMapper;
import com.example.backoffice.Repository.SectionMetadataRepository;

@ExtendWith(MockitoExtension.class)
public class SectionMetadataServiceTests {
    @Mock
    private SectionMetadataRepository sectionMetadataRepository;

    private SectionMetadataMapper sectionMetadataMapper;

    @InjectMocks
    private SectionMetadataServiceImp sectionMetadataServiceImp;

    private SectionMetadata sectionMetadata;
    private SectionMetadata sectionMetadata2;

    private SectionMetadata createSectionMetadata(String id, String name, String desc) {
        SectionMetadata s = new SectionMetadata();
        s.setId(id);
        s.setName(name);
        s.setDescription(desc);
        return s;
    }

    @BeforeEach
    void setup() {
        sectionMetadataMapper = Mappers.getMapper(SectionMetadataMapper.class);
        ReflectionTestUtils.setField(sectionMetadataServiceImp, "sectionMetadataMapper", sectionMetadataMapper);

        sectionMetadata = createSectionMetadata("entrée", "entrée", "Pour commencer");
        sectionMetadata2 = createSectionMetadata("plat", "plat", "a partager");
    }

    @Test
    void shouldGetAll() {
        when(sectionMetadataRepository.findAll()).thenReturn(List.of(sectionMetadata, sectionMetadata2));

        List<SectionMetadataForGetResponseDTO> result = sectionMetadataServiceImp.getAll();

        assertThat(result).hasSize(2)
                .extracting(SectionMetadataForGetResponseDTO::getName)
                .containsExactly(sectionMetadata.getName(), sectionMetadata2.getName());

        verify(sectionMetadataRepository).findAll();
    }

    @Test
    void shouldCreate() {
        SectionMetadata dessert = createSectionMetadata("dessert", "dessert", "pour finir");
        SectionMetadataDTO payload = new SectionMetadataDTO();
        payload.setName("dessert");
        payload.setDescription("pour finir");

        when(sectionMetadataRepository.save(any(SectionMetadata.class))).thenReturn(dessert);

        SectionMetadataResponseDTO result = sectionMetadataServiceImp.create(payload);

        assertThat(result.getId()).isEqualTo(dessert.getId());
        assertThat(result.getName()).isEqualTo(payload.getName().toLowerCase());
        assertThat(result.getDescription()).isEqualTo(payload.getDescription().toLowerCase());

        verify(sectionMetadataRepository).save(any(SectionMetadata.class));
    }

    @Test
    void shouldCreateWithoutDescription() {
        SectionMetadata dessert = createSectionMetadata("dessert", "dessert", null);
        SectionMetadataDTO payload = new SectionMetadataDTO();
        payload.setName("dessert");

        when(sectionMetadataRepository.save(any(SectionMetadata.class))).thenReturn(dessert);

        SectionMetadataResponseDTO result = sectionMetadataServiceImp.create(payload);

        assertThat(result.getId()).isEqualTo(dessert.getId());
        assertThat(result.getName()).isEqualTo(payload.getName().toLowerCase());
        assertThat(result.getDescription()).isNullOrEmpty();
        ;

        verify(sectionMetadataRepository).save(any(SectionMetadata.class));
    }

    @Test
    public void shouldGetById() {
        String id = sectionMetadata.getId();

        when(sectionMetadataRepository.findById(id)).thenReturn(Optional.of(sectionMetadata));

        SectionMetadataForGetResponseDTO result = sectionMetadataServiceImp.getById(id);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo(sectionMetadata.getName());
        assertThat(result.getDescription()).isEqualTo(sectionMetadata.getDescription());

        verify(sectionMetadataRepository).findById(id);
    }

    @Test
    public void shouldThrowWithUnknownId() {
        String id = "non-existing-id";

        when(sectionMetadataRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sectionMetadataServiceImp.getById(id))
                .isInstanceOf(SectionMetadataNotFoundException.class)
                .hasMessageContaining(id);

        verify(sectionMetadataRepository).findById(id);
    }

    @Test
    public void shouldUpdateById() {
        String id = sectionMetadata.getId();

        when(sectionMetadataRepository.findById(id))
                .thenReturn(Optional.of(sectionMetadata));

        SectionMetadataDTO payload = new SectionMetadataDTO();
        payload.setName("special");

        when(sectionMetadataRepository.save(any()))
                .thenReturn(sectionMetadata);

        SectionMetadataResponseDTO result = sectionMetadataServiceImp.updateById(id, payload);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo(payload.getName());
        assertThat(result.getDescription()).isEqualTo(sectionMetadata.getDescription());

        verify(sectionMetadataRepository).findById(id);
        verify(sectionMetadataRepository).save(any());
    }

    @Test
    public void shouldDeleteById() {
        String id = sectionMetadata.getId();

        when(sectionMetadataRepository.findById(id))
                .thenReturn(Optional.of(sectionMetadata));

        sectionMetadataServiceImp.deleteById(id);
        
        verify(sectionMetadataRepository).findById(id);
        verify(sectionMetadataRepository).delete(sectionMetadata);
    }
}
