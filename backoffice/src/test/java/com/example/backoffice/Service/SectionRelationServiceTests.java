package com.example.backoffice.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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

import com.example.backoffice.DTO.sectionRelation.SectionRelationDTO;
import com.example.backoffice.DTO.sectionRelation.SectionRelationResponseDTO;
import com.example.backoffice.Entity.DishMetadata;
import com.example.backoffice.Entity.SectionMetadata;
import com.example.backoffice.Entity.SectionRelation;
import com.example.backoffice.Exception.notFoundException.SectionRelationNotFoundException;
import com.example.backoffice.Mapper.SectionRelationMapper;
import com.example.backoffice.Repository.SectionRelationRepository;

@ExtendWith(MockitoExtension.class)
public class SectionRelationServiceTests {
    @Mock
    private SectionRelationRepository sectionRelationRepository;

    @Mock
    private SectionMetadataService sectionMetadataService;

    @Mock
    private DishMetadataService dishMetadataService;

    private SectionRelationMapper sectionRelationMapper;

    @InjectMocks
    private SectionRelationServiceImp sectionRelationServiceImp;

    private DishMetadata dishMetadata;
    private DishMetadata dishMetadata2;
    private SectionMetadata sectionMetadata;
    private SectionMetadata sectionMetadata2;
    private SectionRelation sectionRelation;

    private DishMetadata createDishMetadata(String id, String name) {
        DishMetadata dishMetadata = new DishMetadata();
        dishMetadata.setId(id);
        dishMetadata.setName(name);
        return dishMetadata;
    }

    private SectionMetadata createSectionMetadata(String id, String name, String desc) {
        SectionMetadata s = new SectionMetadata();
        s.setId(id);
        s.setName(name);
        s.setDescription(desc);
        return s;
    }

    private SectionRelation createSectionRelation(String id, SectionMetadata sectionMetadata,
            DishMetadata DishMetadata) {
        SectionRelation sr = new SectionRelation();
        sr.setId(id);
        sr.setSectionMetadata(sectionMetadata);
        sr.setDishMetadata(DishMetadata);
        return sr;
    }

    @BeforeEach
    void setup() {
        sectionRelationMapper = Mappers.getMapper(SectionRelationMapper.class);
        ReflectionTestUtils.setField(sectionRelationServiceImp, "sectionRelationMapper", sectionRelationMapper);

        dishMetadata = createDishMetadata("dish-1", "bibimpap");
        dishMetadata2 = createDishMetadata("dish-2", "bao");

        sectionMetadata = createSectionMetadata("entrée", "entrée", "Pour commencer");
        sectionMetadata2 = createSectionMetadata("plat", "plat", "a partager");

        sectionRelation = createSectionRelation("relation-1", sectionMetadata, dishMetadata);
    }

    @Test
    void shouldGetAll() {
        when(sectionRelationRepository.findAll()).thenReturn(List.of(sectionRelation));

        List<SectionRelationResponseDTO> result = sectionRelationServiceImp.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDishMetadata().getName()).isEqualTo(dishMetadata.getName());
        verify(sectionRelationRepository).findAll();
    }

    @Test
    void shouldCreate() {
        SectionRelation sectionRelation2 = createSectionRelation("relation-2", sectionMetadata, dishMetadata);
        SectionRelationDTO payload = new SectionRelationDTO();
        payload.setDishMetadataId("dish-1");
        payload.setSectionMetadataId("entrée");

        when(sectionMetadataService.getEntityById(payload.getSectionMetadataId())).thenReturn(sectionMetadata);
        when(dishMetadataService.getEntityById(payload.getDishMetadataId())).thenReturn(dishMetadata);
        when(sectionRelationRepository.save(any(SectionRelation.class))).thenReturn(sectionRelation2);

        SectionRelationResponseDTO result = sectionRelationServiceImp.create(payload);

        assertThat(result.getId()).isEqualTo(sectionRelation2.getId());
        assertThat(result.getDishMetadata().getId()).isEqualTo(dishMetadata.getId());
        assertThat(result.getSectionMetadata().getId()).isEqualTo(sectionMetadata.getId());

        verify(sectionMetadataService).getEntityById(payload.getSectionMetadataId());
        verify(dishMetadataService).getEntityById(payload.getDishMetadataId());
        verify(sectionRelationRepository).save(any(SectionRelation.class));
    }

    @Test
    public void shouldGetById() {
        String id = sectionRelation.getId();

        when(sectionRelationRepository.findById(id)).thenReturn(Optional.of(sectionRelation));

        SectionRelationResponseDTO result = sectionRelationServiceImp.getById(id);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getDishMetadata().getId()).isEqualTo(dishMetadata.getId());
        assertThat(result.getSectionMetadata().getId()).isEqualTo(sectionMetadata.getId());

        verify(sectionRelationRepository).findById(id);
    }

    @Test
    public void shouldThrowWithUnknownId() {
        String id = "non-existing-id";

        when(sectionRelationRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sectionRelationServiceImp.getById(id))
                .isInstanceOf(SectionRelationNotFoundException.class)
                .hasMessageContaining(id);

        verify(sectionRelationRepository).findById(id);
    }

    @Test
    public void shouldUpdateByIdWithoutSectionMedatada() {
        String id = sectionRelation.getId();

        when(sectionRelationRepository.findById(id))
                .thenReturn(Optional.of(sectionRelation));

        SectionRelationDTO payload = new SectionRelationDTO();
        payload.setDishMetadataId(dishMetadata2.getId());

        when(dishMetadataService.getEntityById(payload.getDishMetadataId())).thenReturn(dishMetadata2);
        when(sectionRelationRepository.save(any()))
                .thenReturn(sectionRelation);

        SectionRelationResponseDTO result = sectionRelationServiceImp.updateById(id, payload);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getDishMetadata().getId()).isEqualTo(dishMetadata2.getId());
        assertThat(result.getSectionMetadata().getId()).isEqualTo(sectionRelation.getSectionMetadata().getId());

        verify(sectionRelationRepository).findById(id);
        verifyNoInteractions(sectionMetadataService);
        verify(dishMetadataService).getEntityById(payload.getDishMetadataId());
        verify(sectionRelationRepository).save(any());
    }

    @Test
    public void shouldUpdateByIdWithoutDishMetadata() {
        String id = sectionRelation.getId();

        when(sectionRelationRepository.findById(id))
                .thenReturn(Optional.of(sectionRelation));

        SectionRelationDTO payload = new SectionRelationDTO();
        payload.setSectionMetadataId(sectionMetadata2.getId());

        when(sectionMetadataService.getEntityById(payload.getSectionMetadataId())).thenReturn(sectionMetadata2);
        when(sectionRelationRepository.save(any()))
                .thenReturn(sectionRelation);

        SectionRelationResponseDTO result = sectionRelationServiceImp.updateById(id, payload);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getDishMetadata().getId()).isEqualTo(sectionRelation.getDishMetadata().getId());
        assertThat(result.getSectionMetadata().getId()).isEqualTo(sectionMetadata2.getId());

        verify(sectionRelationRepository).findById(id);
        verify(sectionMetadataService).getEntityById(payload.getSectionMetadataId());
        verifyNoInteractions(dishMetadataService);
        verify(sectionRelationRepository).save(any());
    }

    @Test
    public void shouldDeleteById() {
        String id = sectionRelation.getId();

        when(sectionRelationRepository.findById(id))
                .thenReturn(Optional.of(sectionRelation));

        sectionRelationServiceImp.deleteById(id);

        verify(sectionRelationRepository).findById(id);
        verify(sectionRelationRepository).delete(sectionRelation);
    }
}
