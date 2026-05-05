package com.example.backoffice.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backoffice.DTO.ingredient.IngredientDTO;
import com.example.backoffice.DTO.ingredient.IngredientResponseDTO;
import com.example.backoffice.DTO.ingredient.IngredientUpdateDTO;
import com.example.backoffice.Service.IngredientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/ingredients")
@Tag(name = "Ingredient", description = "Gestion des ingrédients")
public class IngredientController {
    @Autowired
    private IngredientService ingredientService;

    @Operation(summary = "Récupérer toutes les ingrédients")
    @ApiResponse(responseCode = "200", description = "Liste des ingrédients récupérée avec succès")
    @GetMapping("")
    public @ResponseBody List<IngredientResponseDTO> getAll() {
        return ingredientService.getAll();
    }

    @Operation(summary = "Créer un ingrédient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ingrédient créée"),
            @ApiResponse(responseCode = "400", description = "Payload pas valid") })
    @PostMapping("")
    public ResponseEntity<IngredientResponseDTO> create(@RequestBody @Valid IngredientDTO payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredientService.create(payload));

    }

    @Operation(summary = "Récupérer un ingrédient par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingrédient trouvée"),
            @ApiResponse(responseCode = "404", description = "Ingrédient non trouvée")
    })
    @GetMapping("/{id}")
    public IngredientResponseDTO getById(@PathVariable String id) {
        return ingredientService.getById(id);
    }

    @Operation(summary = "Mettre à jour un ingrédient")
    @ApiResponse(responseCode = "200", description = "ingrédient mise à jour")
    @PutMapping("/{id}")
    public IngredientResponseDTO updateById(@PathVariable String id, @RequestBody @Valid IngredientUpdateDTO payload) {
        return ingredientService.updateById(id, payload);
    }

    @Operation(summary = "Supprimer un ingrédient")
    @ApiResponse(responseCode = "204", description = "Ingrédient supprimée")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        ingredientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
