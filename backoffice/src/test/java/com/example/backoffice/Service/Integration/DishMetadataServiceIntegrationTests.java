package com.example.backoffice.Service.Integration;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.*;

import com.example.backoffice.DTO.dishMetadata.DishMetadataDTO;
import com.example.backoffice.DTO.dishMetadata.DishMetadataForGetResponseDTO;
import com.example.backoffice.DTO.dishMetadata.DishMetadataResponseDTO;
import com.example.backoffice.DTO.dishMetadata.DishMetadataUpdateDTO;
import com.example.backoffice.Entity.DishMetadata;
import com.example.backoffice.Exception.notFoundException.DishMetadataNotFoundException;
import com.example.backoffice.Repository.DishMetadataRepository;
import com.example.backoffice.Service.DishMetadataServiceImp;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class DishMetadataServiceIntegrationTests {
    @Autowired
    private DishMetadataRepository dishMetadataRepository;
    @Autowired
    private DishMetadataServiceImp dishMetadataServiceImp;

    private DishMetadata dishMetadata1;
    private DishMetadata dishMetadata2;
    private String unknownId;

    private DishMetadata createEntity(String name, String description, BigDecimal price) {
        DishMetadata entity = new DishMetadata();
        entity.setName(name);
        entity.setDescription(description);
        entity.setPrice(price);
        return dishMetadataRepository.save(entity);
    }

    @BeforeEach
    void setup() {
        unknownId = "non-existing-id";
        dishMetadataRepository.deleteAll();

        dishMetadata1 = createEntity("oeuf mayonnaise",
                "entrée froide composée d’œufs durs coupés en deux",
                BigDecimal.valueOf(5.50));

        dishMetadata2 = createEntity("bibimbap poulet",
                "du riz blanc donc, escorté de plusieurs garnitures de légumes sautés, de viande",
                BigDecimal.valueOf(15.50));
    }

    @Test
    void shouldGetAll() {
        List<DishMetadataForGetResponseDTO> result = dishMetadataServiceImp.getAll();

        assertThat(result)
                .hasSize(2)
                .extracting(DishMetadataForGetResponseDTO::getName)
                .containsExactlyInAnyOrder(
                        dishMetadata1.getName(),
                        dishMetadata2.getName());
    }

    @Test
    void shouldCreate() {
        DishMetadataDTO payload = new DishMetadataDTO();
        payload.setName("bao");
        payload.setDescription("plat au porc");
        payload.setPrice(BigDecimal.valueOf(5.0));

        DishMetadataResponseDTO result = dishMetadataServiceImp.create(payload);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(payload.getName());
        assertThat(result.getDescription()).isEqualTo(payload.getDescription());
        assertThat(result.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(5.0));

        DishMetadata resultInDatabase = dishMetadataRepository.findById(result.getId())
                .orElseThrow();

        assertThat(resultInDatabase.getName()).isEqualTo(payload.getName());
        assertThat(resultInDatabase.getDescription()).isEqualTo(payload.getDescription());
        assertThat(resultInDatabase.getPrice()).isEqualByComparingTo(payload.getPrice());
    }

    @Test
    void shouldCreateWithoutDescription() {
        DishMetadataDTO payload = new DishMetadataDTO();
        payload.setName("Bibimbap");
        payload.setPrice(BigDecimal.valueOf(15));

        DishMetadataResponseDTO result = dishMetadataServiceImp.create(payload);

        assertThat(result.getName()).isEqualTo(payload.getName().toLowerCase());
        assertThat(result.getDescription()).isNull();

        assertThat(result.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(15));

        DishMetadata resultInDatabase = dishMetadataRepository.findById(result.getId())
                .orElseThrow();

        assertThat(resultInDatabase.getName()).isEqualTo(payload.getName().toLowerCase());
        assertThat(resultInDatabase.getDescription()).isNull();
    }

    @Test
    void shouldGetById() {
        String id = dishMetadata1.getId();

        DishMetadataForGetResponseDTO result = dishMetadataServiceImp.getById(id);

        assertThat(result).isNotNull();
        assertThat(result.getId())
                .isEqualTo(dishMetadata1.getId());
        assertThat(result.getName())
                .isEqualTo(dishMetadata1.getName());
        assertThat(result.getDescription())
                .isEqualTo(dishMetadata1.getDescription());
        assertThat(result.getPrice())
                .isEqualByComparingTo(dishMetadata1.getPrice());
    }

    @Test
    void shouldThrowWithUnknownId() {
        assertThat(dishMetadataRepository.findById(unknownId)).isEmpty();

        assertThatThrownBy(() -> dishMetadataServiceImp.getById(unknownId))
                .isInstanceOf(DishMetadataNotFoundException.class)
                .hasMessageContaining(unknownId);
    }

    @Test
    void shouldUpdateById() {
        String id = dishMetadata1.getId();

        DishMetadataUpdateDTO payload = new DishMetadataUpdateDTO();
        payload.setName("oeuf mimosa");

        DishMetadataResponseDTO result = dishMetadataServiceImp.updateById(id, payload);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(dishMetadata1.getId());
        assertThat(result.getName()).isEqualTo(payload.getName());
        assertThat(result.getDescription()).isEqualTo(dishMetadata1.getDescription());
        assertThat(result.getPrice()).isEqualByComparingTo(dishMetadata1.getPrice());

        DishMetadata updatedInDatabase = dishMetadataRepository.findById(id).orElseThrow();

        assertThat(updatedInDatabase.getName()).isEqualTo(payload.getName());
        assertThat(updatedInDatabase.getDescription()).isEqualTo(dishMetadata1.getDescription());
        assertThat(updatedInDatabase.getPrice()).isEqualByComparingTo(dishMetadata1.getPrice());
    }

    @Test
    void shouldDeleteById() {
        // Given
        String id = dishMetadata1.getId();
        // When
        dishMetadataServiceImp.deleteById(id);
        // Then
        boolean exists = dishMetadataRepository.existsById(id);
        assertThat(exists).isFalse();
    }

}
