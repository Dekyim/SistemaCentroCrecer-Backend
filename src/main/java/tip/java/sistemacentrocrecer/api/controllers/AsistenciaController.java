package tip.java.sistemacentrocrecer.api.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.AsistenciaService;
import tip.java.sistemacentrocrecer.dto.AsistenciaRequestDTO;
import tip.java.sistemacentrocrecer.dto.AsistenciaResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/asistencias")
@AllArgsConstructor
public class AsistenciaController {
    private final AsistenciaService asistenciaService;

    @GetMapping
    public ResponseEntity<List<AsistenciaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(asistenciaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsistenciaResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(asistenciaService.obtenerPorId(id));
    }

    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<AsistenciaResponseDTO> obtenerPorCedula(@PathVariable String cedula) {
        return ResponseEntity.ok(asistenciaService.obtenerPorCedula(cedula));
    }

    @PostMapping
    public ResponseEntity<AsistenciaResponseDTO> crear(@RequestBody AsistenciaRequestDTO dto) {
        return ResponseEntity.ok(asistenciaService.crear(dto));
    }

    @PutMapping("/{id}/baja")
    public ResponseEntity<Void> darDeBaja(@PathVariable Integer id) {
        asistenciaService.darDeBaja(id);
        return ResponseEntity.noContent().build();
    }
}
