package com.example.backoffice.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse; 
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backoffice.DTO.sectionMetadata.SectionMetadataDTO;
import com.example.backoffice.DTO.sectionMetadata.SectionMetadataForGetResponseDTO;
import com.example.backoffice.DTO.sectionMetadata.SectionMetadataResponseDTO;
import com.example.backoffice.Service.SectionMetadataService;

@RestController
@RequestMapping("/sectionMetadata")
@Tag(name = "Section Metadata", description = "Gestion des données d'une section")
public class SectionMetadataController {
    @Autowired
    private SectionMetadataService sectionMetadataService;

    @Operation(summary = "Récupérer toutes les données de section")
    @ApiResponse(responseCode = "200", description = "Liste des données de section récupérée avec succès")
    @GetMapping("")
    public @ResponseBody List<SectionMetadataForGetResponseDTO> getAll() {
        return sectionMetadataService.getAll();
    }

    @Operation(summary = "Créer une fiche de section")
    @ApiResponse(responseCode = "200", description = "Fiche de section créée")
    @PostMapping("")
    public @ResponseBody SectionMetadataResponseDTO create(@RequestBody SectionMetadataDTO payload) {
        return sectionMetadataService.create(payload);
    }

    @Operation(summary = "Récupérer une fiche section par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fiche section trouvée"),
            @ApiResponse(responseCode = "404", description = "Fiche section non trouvée")
    })
    @GetMapping("/{id}")
    public SectionMetadataForGetResponseDTO getById(@PathVariable String id) {
        return sectionMetadataService.getById(id);
    }

    @Operation(summary = "Mettre à jour une fiche section")
    @ApiResponse(responseCode = "200", description = "Fiche section mis à jour")
    @PutMapping("/{id}")
    public SectionMetadataResponseDTO updateById(@PathVariable String id, @RequestBody SectionMetadataDTO payload) {
        return sectionMetadataService.updateById(id, payload);
    }

    @Operation(summary = "Supprimer une fiche section")
    @ApiResponse(responseCode = "204", description = "Fiche section supprimée")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        sectionMetadataService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
