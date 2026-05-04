package tip.java.sistemacentrocrecer.api.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.GrupoService;
import tip.java.sistemacentrocrecer.dto.GrupoRequestDTO;
import tip.java.sistemacentrocrecer.dto.GrupoResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/grupos")
@AllArgsConstructor
public class GrupoController {
    private final GrupoService grupoService;

    @GetMapping
    public ResponseEntity<List<GrupoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(grupoService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<GrupoResponseDTO> crear(@RequestBody GrupoRequestDTO dto) {
        return ResponseEntity.status(201).body(grupoService.crear(dto));
    }

    @PutMapping("/{id}/baja")
    public ResponseEntity<Void> darDeBaja(@PathVariable Integer id) {
        grupoService.darDeBaja(id);
        return ResponseEntity.noContent().build();
    }
}
