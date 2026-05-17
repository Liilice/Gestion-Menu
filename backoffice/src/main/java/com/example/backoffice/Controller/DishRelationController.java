package com.example.backoffice.Controller;

import com.example.backoffice.DTO.dishRelation.DishRelationDTO;
import com.example.backoffice.DTO.dishRelation.DishRelationResponseDTO;
import com.example.backoffice.DTO.dishRelation.DishRelationUpdateDTO;
import com.example.backoffice.Service.DishRelationService;

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
@RequestMapping("/dishRelation")
@Tag(name = "Dish Relation", description = "Gestion de la relation d'un plat")
public class DishRelationController {
    @Autowired
    private DishRelationService dishRelationService;

    @Operation(summary = "Récupérer toutes les relations de plats")
    @ApiResponse(responseCode = "200", description = "Liste les relations de plats récupérée avec succès")
    @GetMapping("")
    public @ResponseBody List<DishRelationResponseDTO> getAll() {
        return dishRelationService.getAll();
    }

    @Operation(summary = "Créer la relation d'un plat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Relation créée"),
            @ApiResponse(responseCode = "400", description = "Payload pas valid") })
    @PostMapping("")
    public ResponseEntity<DishRelationResponseDTO> create(@RequestBody @Valid DishRelationDTO payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dishRelationService.create(payload));
    }

    @Operation(summary = "Récupérer une relation d'un plat par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relation du plat trouvée"),
            @ApiResponse(responseCode = "404", description = "Relation du plat non trouvée")
    })
    @GetMapping("/{id}")
    public DishRelationResponseDTO getById(@PathVariable String id) {
        return dishRelationService.getById(id);
    }

    @Operation(summary = "Mettre à jour une relation de plat par ID")
    @ApiResponse(responseCode = "200", description = "Relation de plat mise à jour")
    @PutMapping("/{id}")
    public DishRelationResponseDTO updateById(@PathVariable String id, @RequestBody DishRelationUpdateDTO payload) {
        return dishRelationService.updateById(id, payload);
    }

    @Operation(summary = "Supprimer une relation de plat par ID")
    @ApiResponse(responseCode = "204", description = "Relation de plat supprimée")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        dishRelationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
