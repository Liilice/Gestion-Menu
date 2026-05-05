package com.example.backoffice.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backoffice.DTO.sectionRelation.SectionRelationDTO;
import com.example.backoffice.DTO.sectionRelation.SectionRelationResponseDTO;
import com.example.backoffice.Service.SectionRelationService;

@RestController
@RequestMapping("/sectionRelation")
@Tag(name = "Section Relation", description = "Gestion des relation des sections")
public class SectionRelationController {
    @Autowired
    private SectionRelationService sectionRelationService;

    @Operation(summary = "Récupérer toutes les relations de section")
    @ApiResponse(responseCode = "200", description = "Liste des relation de section récupérée avec succès")
    @GetMapping("")
    public @ResponseBody List<SectionRelationResponseDTO> getAll() {
        return sectionRelationService.getAll();
    }

    @Operation(summary = "Créer une relation de section")
    @ApiResponse(responseCode = "200", description = "Relation de section créée")
    @PostMapping("")
    public @ResponseBody SectionRelationResponseDTO create(@RequestBody SectionRelationDTO payload) {
        return sectionRelationService.create(payload);
    }

    @Operation(summary = "Récupérer une relation section par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relation section trouvée"),
            @ApiResponse(responseCode = "404", description = "Relation section non trouvée")
    })
    @GetMapping("/{id}")
    public SectionRelationResponseDTO getById(@PathVariable String id) {
        return sectionRelationService.getById(id);
    }

    @Operation(summary = "Mettre à jour une relation section")
    @ApiResponse(responseCode = "200", description = "Relation section mis à jour")
    @PutMapping("/{id}")
    public SectionRelationResponseDTO updateById(@PathVariable String id, @RequestBody SectionRelationDTO payload) {
        return sectionRelationService.updateById(id, payload);
    }

    @Operation(summary = "Supprimer une relation section")
    @ApiResponse(responseCode = "204", description = "Relation section supprimée")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        sectionRelationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
