package com.example.backoffice.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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

import com.example.backoffice.DTO.dishMetadata.DishMetadataDTO;
import com.example.backoffice.DTO.dishMetadata.DishMetadataForGetResponseDTO;
import com.example.backoffice.DTO.dishMetadata.DishMetadataResponseDTO;
import com.example.backoffice.DTO.dishMetadata.DishMetadataUpdateDTO;
import com.example.backoffice.Entity.DishMetadata;
import com.example.backoffice.Exception.notFoundException.DishMetadataNotFoundException;
import com.example.backoffice.Mapper.DishMetadataMapper;
import com.example.backoffice.Repository.DishMetadataRepository;

@ExtendWith(MockitoExtension.class)
public class DishMetadataServiceTests {
        @Mock
        private DishMetadataRepository dishMetadataRepository;

        private DishMetadataMapper dishMetadataMapper;

        @InjectMocks
        private DishMetadataServiceImp dishMetadataServiceImp;

        private DishMetadata bibimpap;
        private DishMetadata bao;

        private DishMetadata createDishMetadata(DishMetadataDTO payload) {
                DishMetadata dishMetadata = new DishMetadata();
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
        public void setup() {
                dishMetadataMapper = Mappers.getMapper(DishMetadataMapper.class);

                bibimpap = createDishMetadata(
                                createPayload("bibimpap", "plat coréen", BigDecimal.valueOf(12.50)));

                bao = createDishMetadata(
                                createPayload("bao", "plat chinois", BigDecimal.valueOf(8.00)));

                ReflectionTestUtils.setField(dishMetadataServiceImp, "dishMetadataMapper", dishMetadataMapper);

        }

        @Test
        public void shouldGetAll() {
                when(dishMetadataRepository.findAll()).thenReturn(List.of(bibimpap, bao));

                List<DishMetadataForGetResponseDTO> result = dishMetadataServiceImp.getAll();

                assertThat(result)
                                .hasSize(2)
                                .extracting(DishMetadataForGetResponseDTO::getName)
                                .containsExactly(bibimpap.getName(), bao.getName());

                verify(dishMetadataRepository).findAll();
        }

        @Test
        public void shouldGetAllWithAnEmptyArray() {
                when(dishMetadataRepository.findAll()).thenReturn(List.of());
                List<DishMetadataForGetResponseDTO> result = dishMetadataServiceImp.getAll();
                assertThat(result).hasSize(0).isEmpty();
                verify(dishMetadataRepository).findAll();
        }

        @Test
        public void shouldCreate() {
                DishMetadata carbonara = createDishMetadata(
                                createPayload("carbonara", "plat italien", BigDecimal.valueOf(11.50)));

                DishMetadataDTO payload = createPayload("carbonara", "plat italien", BigDecimal.valueOf(11.50));

                when(dishMetadataRepository.save(any(DishMetadata.class)))
                                .thenReturn(carbonara);

                DishMetadataResponseDTO result = dishMetadataServiceImp.create(payload);
                assertThat(result.getName()).isEqualTo(carbonara.getName());
                assertThat(result.getDescription()).isEqualTo(carbonara.getDescription());
                assertThat(result.getPrice()).isEqualTo(carbonara.getPrice());

                verify(dishMetadataRepository).save(any(DishMetadata.class));
        }

        @Test
        public void shouldCreateWithNullDesctiption() {
                DishMetadata carbonara = createDishMetadata(
                                createPayload("carbonara", null, BigDecimal.valueOf(11.50)));

                DishMetadataDTO payload = createPayload("carbonara", null, BigDecimal.valueOf(11.50));

                when(dishMetadataRepository.save(any(DishMetadata.class)))
                                .thenReturn(carbonara);

                DishMetadataResponseDTO result = dishMetadataServiceImp.create(payload);
                assertThat(result.getDescription()).isNullOrEmpty();

                verify(dishMetadataRepository).save(any(DishMetadata.class));
        }

        @Test
        public void shouldGetById() {
                String id = bibimpap.getId();

                when(dishMetadataRepository.findById(id)).thenReturn(Optional.of(bibimpap));

                DishMetadataForGetResponseDTO result = dishMetadataServiceImp.getById(id);
                assertThat(result.getId()).isEqualTo(id);
                assertThat(result.getName()).isEqualTo(bibimpap.getName());
                assertThat(result.getDescription()).isEqualTo(bibimpap.getDescription());
                assertThat(result.getPrice()).isEqualTo(bibimpap.getPrice());

                verify(dishMetadataRepository).findById(id);
        }

        @Test
        public void shouldThrowWithUnknownId() {
                String id = "non-existing-id";
                when(dishMetadataRepository.findById(id)).thenReturn(Optional.empty());

                assertThatThrownBy(() -> dishMetadataServiceImp.getById(id))
                                .isInstanceOf(DishMetadataNotFoundException.class)
                                .hasMessageContaining(id);

                verify(dishMetadataRepository).findById(id);
        }

        @Test
        public void shouldUpdateById() {
                String id = bibimpap.getId();

                when(dishMetadataRepository.findById(id))
                                .thenReturn(Optional.of(bibimpap));

                DishMetadataUpdateDTO payload = new DishMetadataUpdateDTO();
                payload.setPrice(BigDecimal.valueOf(20.2));

                when(dishMetadataRepository.save(any()))
                                .thenReturn(bibimpap);

                dishMetadataServiceImp.updateById(id, payload);

                assertThat(bibimpap.getPrice())
                                .isEqualByComparingTo("20.2");

                verify(dishMetadataRepository).save(any());
        }

        @Test
        public void shouldDeleteById() {
                String id = bibimpap.getId();

                when(dishMetadataRepository.findById(id))
                                .thenReturn(Optional.of(bibimpap));

                dishMetadataServiceImp.deleteById(id);

                verify(dishMetadataRepository).findById(id);
                verify(dishMetadataRepository).delete(bibimpap);
        }

        @Test
        public void shouldThrowWhenDeletingUnknownId() {
                String id = "non-existing-id";
                when(dishMetadataRepository.findById(id)).thenReturn(Optional.empty());
                assertThatThrownBy(() -> dishMetadataServiceImp.deleteById(id))
                                .isInstanceOf(DishMetadataNotFoundException.class)
                                .hasMessage("DishMetadata not found with id: " + id)
                                .hasMessageContaining(id);
                verify(dishMetadataRepository).findById(id);
                verify(dishMetadataRepository, never()).delete(any());
        }
}
