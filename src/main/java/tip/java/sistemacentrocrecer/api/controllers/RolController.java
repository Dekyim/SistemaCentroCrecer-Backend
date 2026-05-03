package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.RolService;
import tip.java.sistemacentrocrecer.dto.RolResponseDTO;
import tip.java.sistemacentrocrecer.dto.RolRequestDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@AllArgsConstructor
public class RolController {
    private final RolService rolService;

    @GetMapping
    public ResponseEntity<List<RolRequestDTO>> listarTodos() {
        return ResponseEntity.ok(rolService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<RolRequestDTO>> listarActivos() {
        return ResponseEntity.ok(rolService.listarActivos());
    }

    @PostMapping
    public ResponseEntity<RolRequestDTO> crear(@Valid @RequestBody RolResponseDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rolService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolRequestDTO> actualizar(@PathVariable Integer id,
                                                    @Valid @RequestBody RolResponseDTO dto) {
        return ResponseEntity.ok(rolService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/baja")
    public ResponseEntity<Void> darDeBaja(@PathVariable Integer id) {
        rolService.darDeBaja(id);
        return ResponseEntity.noContent().build();
    }


}
