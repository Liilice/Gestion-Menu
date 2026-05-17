package com.example.backoffice.Controller;

import com.example.backoffice.DTO.category.CategoryDTO;
import com.example.backoffice.DTO.category.CategoryResponseDTO;
import com.example.backoffice.Service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@Tag(name = "Category", description = "Gestion des catégories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Récupérer toutes les catégories")
    @ApiResponse(responseCode = "200", description = "Liste des catégories récupérée avec succès")
    @GetMapping("")
    public @ResponseBody List<CategoryResponseDTO> getAll() {
        return categoryService.getAll();
    }

    @Operation(summary = "Créer une catégorie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Catégorie créée"),
            @ApiResponse(responseCode = "400", description = "Payload pas valid") })
    @PostMapping("")
    public ResponseEntity<CategoryResponseDTO> create(@RequestBody @Valid CategoryDTO payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(payload));
    }

    @Operation(summary = "Récupérer une catégorie par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catégorie trouvée"),
            @ApiResponse(responseCode = "404", description = "Catégorie non trouvée")
    })
    @GetMapping("/{id}")
    public CategoryResponseDTO getById(@PathVariable String id) {
        return categoryService.getById(id);
    }

    @Operation(summary = "Mettre à jour une catégorie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catégorie mise à jour"),
            @ApiResponse(responseCode = "404", description = "Catégorie non trouvée"),
            @ApiResponse(responseCode = "400", description = "Payload invalide")
    })
    @PutMapping("/{id}")
    public CategoryResponseDTO updateById(@PathVariable String id, @RequestBody @Valid CategoryDTO payload) {
        return categoryService.updateById(id, payload);
    }

    @Operation(summary = "Supprimer une catégorie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Catégorie supprimée"),
            @ApiResponse(responseCode = "404", description = "Catégorie non trouvée")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Supprimer plusieurs catégories")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Catégorie supprimée"),
        @ApiResponse(responseCode = "404", description = "Catégorie non trouvée")
    })
    @DeleteMapping("/many-by-id")
    public ResponseEntity<Void> deleteById(@RequestBody List<String> idsList ) {
        categoryService.deleteManyById(idsList);
        return ResponseEntity.noContent().build();
    }
}
