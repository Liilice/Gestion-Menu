package com.example.backoffice.Controller;

import com.example.backoffice.DTO.dishMetadata.DishMetadataDTO;
import com.example.backoffice.DTO.dishMetadata.DishMetadataForGetResponseDTO;
import com.example.backoffice.DTO.dishMetadata.DishMetadataResponseDTO;
import com.example.backoffice.DTO.dishMetadata.DishMetadataUpdateDTO;
import com.example.backoffice.Service.DishMetadataService;
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
@RequestMapping("/dishMetadata")
@Tag(name = "Dish Metadata", description = "Gestion de la description d'un plat ( ex: nom, description, prix )")
public class DishMetadataController {
    @Autowired
    private DishMetadataService dishMetadataService;

    @Operation(summary = "Récupérer toutes les fiches de plats")
    @ApiResponse(responseCode = "200", description = "Liste des fiches de plats récupérée avec succès")
    @GetMapping("")
    public @ResponseBody List<DishMetadataForGetResponseDTO> getAll() {
        return dishMetadataService.getAll();
    }

    @Operation(summary = "Créer une fiche de plat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fiche de plat créée"),
            @ApiResponse(responseCode = "400", description = "Payload pas valid") })
    @PostMapping("")
    public ResponseEntity<DishMetadataResponseDTO> create(@RequestBody @Valid DishMetadataDTO payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dishMetadataService.create(payload));
    }

    @Operation(summary = "Récupérer une fiche d'un plat par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fiche du plat trouvée"),
            @ApiResponse(responseCode = "404", description = "Fiche du plat non trouvée")
    })
    @GetMapping("/{id}")
    public DishMetadataForGetResponseDTO getById(@PathVariable String id) {
        return dishMetadataService.getById(id);
    }

    @Operation(summary = "Mettre à jour une fiche de plat par ID")
    @ApiResponse(responseCode = "200", description = "Fiche de plat mise à jour")
    @PutMapping("/{id}")
    public DishMetadataResponseDTO updateById(@PathVariable String id,
            @RequestBody @Valid DishMetadataUpdateDTO payload) {
        return dishMetadataService.updateById(id, payload);
    }

    @Operation(summary = "Supprimer une fiche de plat par ID")
    @ApiResponse(responseCode = "204", description = "Fiche de plat supprimée")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        dishMetadataService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
