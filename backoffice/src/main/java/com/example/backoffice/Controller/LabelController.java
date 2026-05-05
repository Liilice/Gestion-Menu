package com.example.backoffice.Controller;

import com.example.backoffice.DTO.label.LabelDTO;
import com.example.backoffice.DTO.label.LabelResponseDTO;
import com.example.backoffice.Service.LabelService;

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
@RequestMapping("/label")
@Tag(name = "Label", description = "Gestion des labels ( ex: végétarien, sans gluten, epicé, etc. )")
public class LabelController {
    @Autowired
    private LabelService labelService;

    @Operation(summary = "Récupérer toutes les labels")
    @ApiResponse(responseCode = "200", description = "Liste des labels récupérée avec succès")
    @GetMapping("")
    public @ResponseBody List<LabelResponseDTO> getAll() {
        return labelService.getAll();
    }

    @Operation(summary = "Créer un label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Label créée"),
            @ApiResponse(responseCode = "400", description = "Payload pas valid") })
    @PostMapping("")
    public ResponseEntity<LabelResponseDTO> create(@RequestBody @Valid LabelDTO payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(labelService.create(payload));
    }

    @Operation(summary = "Récupérer un label par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label trouvée"),
            @ApiResponse(responseCode = "404", description = "Label non trouvée")
    })
    @GetMapping("/{id}")
    public LabelResponseDTO getById(@PathVariable String id) {
        return labelService.getById(id);
    }

    @Operation(summary = "Mettre à jour un label")
    @ApiResponse(responseCode = "200", description = "Label mise à jour")
    @PutMapping("/{id}")
    public LabelResponseDTO updateById(@PathVariable String id, @RequestBody @Valid LabelDTO payload) {
        return labelService.updateById(id, payload);
    }

    @Operation(summary = "Supprimer un label")
    @ApiResponse(responseCode = "204", description = "Label supprimée")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        labelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
