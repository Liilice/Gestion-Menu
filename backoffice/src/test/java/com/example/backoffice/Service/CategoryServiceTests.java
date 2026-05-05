package com.example.backoffice.Service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.backoffice.DTO.category.CategoryDTO;
import com.example.backoffice.DTO.category.CategoryResponseDTO;
import com.example.backoffice.DTO.ingredient.IngredientSummaryDTO;
import com.example.backoffice.Entity.Category;
import com.example.backoffice.Exception.notFoundException.CategoryNotFoundException;
import com.example.backoffice.Mapper.CategoryMapper;
import com.example.backoffice.Repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private CategoryDeletionService categoryDeletionService;
    @InjectMocks
    private CategoryServiceImp categoryServiceImp;

    private Category vegetables;
    private Category proteins;

    private Category createCategory(String name) {
        Category c = new Category();
        c.setId("id-" + name.toLowerCase());
        c.setName(name.toLowerCase());
        return c;
    }

    @BeforeEach
    public void setup() {
        vegetables = createCategory("légumes");
        proteins = createCategory("protéines");
    }

    @Test
    public void shouldGetAll() {
        when(categoryRepository.findAll()).thenReturn(List.of(vegetables, proteins));

        when(categoryMapper.toResponseDTO(any()))
                .thenAnswer(invocation -> {
                    Category c = invocation.getArgument(0);
                    CategoryResponseDTO dto = new CategoryResponseDTO();
                    dto.setId(c.getId());
                    dto.setName(c.getName());
                    return dto;
                });

        List<CategoryResponseDTO> categoryResponseDTO = categoryServiceImp.getAll();
        assertThat(categoryResponseDTO)
                .hasSize(2)
                .extracting(CategoryResponseDTO::getName)
                .containsExactly("légumes", "protéines");

        verify(categoryRepository).findAll();
        verify(categoryMapper, times(2)).toResponseDTO(any());
    }

    @Test
    public void shouldGetAllWithAnEmptyArray() {
        when(categoryRepository.findAll()).thenReturn(List.of());
        List<CategoryResponseDTO> categoryResponseDTO = categoryServiceImp.getAll();
        assertThat(categoryResponseDTO).hasSize(0).isEmpty();
        verify(categoryRepository).findAll();
    }

    @Test
    public void shouldCreate() {
        CategoryDTO payload = new CategoryDTO();
        payload.setName("fruit");

        Category savedCategory = createCategory("fruit");

        when(categoryRepository.save(argThat(c -> c.getName().equals("fruit")))).thenReturn(savedCategory);

        when(categoryMapper.toResponseDTO(savedCategory))
                .thenAnswer(invocation -> {
                    Category c = invocation.getArgument(0);
                    CategoryResponseDTO dto = new CategoryResponseDTO();
                    dto.setName(c.getName());
                    return dto;
                });

        CategoryResponseDTO result = categoryServiceImp.create(payload);

        assertThat(result.getName()).isEqualTo("fruit");

        verify(categoryRepository).save(any(Category.class));
        verify(categoryMapper).toResponseDTO(savedCategory);
    }

    @Test
    public void shouldGetById() {
        String id = vegetables.getId();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(vegetables));
        when(categoryMapper.toResponseDTO(vegetables)).thenAnswer(invocation -> {
            Category c = invocation.getArgument(0);
            CategoryResponseDTO dto = new CategoryResponseDTO();
            dto.setId(id);
            dto.setName(c.getName());
            return dto;
        });

        CategoryResponseDTO result = categoryServiceImp.getById(id);
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo(vegetables.getName());
        verify(categoryRepository).findById(id);
        verify(categoryMapper).toResponseDTO(vegetables);
    }

    @Test
    public void shouldThrowWithUnknownId() {
        String id = "non-existing-id";
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryServiceImp.getById(id))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining(id);

        verify(categoryRepository).findById(id);
    }

    @Test
    public void shouldUpdateById() {
        String id = vegetables.getId();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(vegetables));

        CategoryDTO payload = new CategoryDTO();
        payload.setName("légumes frais");

        vegetables.setName("légumes frais");
        when(categoryRepository.save(argThat(c -> c.getName().equals("légumes frais")))).thenReturn(vegetables);

        when(categoryMapper.toResponseDTO(vegetables)).thenAnswer(invocation -> {
            Category c = invocation.getArgument(0);
            CategoryResponseDTO dto = new CategoryResponseDTO();
            dto.setId(id);
            dto.setName(c.getName());
            return dto;
        });

        CategoryResponseDTO result = categoryServiceImp.updateById(id, payload);
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("légumes frais");
        verify(categoryRepository).findById(id);
        verify(categoryRepository).save(any(Category.class));
        verify(categoryMapper).toResponseDTO(vegetables);
    }

    @Test
    void shouldThrowWhenUpdatingUnknownId() {
        String id = "non-existing-id";

        CategoryDTO payload = new CategoryDTO();
        payload.setName("new name");

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryServiceImp.updateById(id, payload))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining(id);

        verify(categoryRepository).findById(id);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    public void shouldDeleteByIdWhenCategoryHasNoIngredients() {
        String id = vegetables.getId();

        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(id);
        dto.setName(vegetables.getName());
        dto.setIngredients(List.of());

        when(categoryRepository.findById(id))
                .thenReturn(Optional.of(vegetables));

        when(categoryMapper.toResponseDTO(vegetables))
                .thenReturn(dto);

        categoryServiceImp.deleteById(id);

        verify(categoryRepository).findById(id);
        verify(categoryMapper).toResponseDTO(vegetables);
        verify(categoryRepository).delete(vegetables);
        verifyNoInteractions(categoryDeletionService);
    }

    @Test
    public void shouldThrowWhenDeletingUnknownId() {
        String id = "non-existing-id";

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryServiceImp.deleteById(id))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining(id);

        verify(categoryRepository).findById(id);
        verify(categoryRepository, never()).delete(any());
    }

    @Test
    void shouldDeleteByIdAndDetachIngredients() {
        String id = vegetables.getId();

        IngredientSummaryDTO ingredient = new IngredientSummaryDTO();
        ingredient.setId("ingredient-1");

        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(id);
        dto.setName(vegetables.getName());
        dto.setIngredients(List.of(ingredient));

        when(categoryRepository.findById(id)).thenReturn(Optional.of(vegetables));
        when(categoryMapper.toResponseDTO(vegetables)).thenReturn(dto);

        categoryServiceImp.deleteById(id);

        verify(categoryDeletionService).setCategoryNull("ingredient-1");
        verify(categoryRepository).delete(vegetables);
    }
}
