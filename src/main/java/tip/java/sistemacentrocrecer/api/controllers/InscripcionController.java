package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.InscripcionService;
import tip.java.sistemacentrocrecer.dto.InscripcionRequestDTO;
import tip.java.sistemacentrocrecer.dto.InscripcionResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inscripciones")
@RequiredArgsConstructor
@CrossOrigin("*")
public class InscripcionController {
    private final InscripcionService inscripcionService;

    @GetMapping
    public ResponseEntity<List<InscripcionResponseDTO>> listarTodos() {
        return ResponseEntity.ok(inscripcionService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InscripcionResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(inscripcionService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<InscripcionResponseDTO> crear(@Valid @RequestBody InscripcionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inscripcionService.crear(dto));
    }

    @PutMapping("/{id}/actualizar")
    public ResponseEntity<InscripcionResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody InscripcionRequestDTO dto) {
        return ResponseEntity.ok(inscripcionService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}/eliminar")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        inscripcionService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
