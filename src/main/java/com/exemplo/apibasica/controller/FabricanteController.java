package com.exemplo.apibasica.controller;

import com.exemplo.apibasica.dto.FabricanteDTO;
import com.exemplo.apibasica.model.Fabricante;
import com.exemplo.apibasica.service.FabricanteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/fabricantes")
public class FabricanteController {

    private final FabricanteService fabricanteService;

    public FabricanteController(FabricanteService fabricanteService) {
        this.fabricanteService = fabricanteService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listaFabricantes() {
        log.info("Listando fabricantes!");

        List<FabricanteDTO> fabricantes = fabricanteService.getAllFabricantes().stream().map(this::convertToDTO).collect(Collectors.toList());

        Map<String, Object> successResponse = Map.of(
                "status", "success",
                "data", fabricantes
        );

        return ResponseEntity.ok(successResponse);
    }

    // Métodos auxiliares para conversão
    private FabricanteDTO convertToDTO(Fabricante fabricante) {
        FabricanteDTO dto = new FabricanteDTO();
        dto.setId(fabricante.getId());
        dto.setNome(fabricante.getNome());
        return dto;
    }
}
