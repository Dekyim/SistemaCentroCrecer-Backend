package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.PermisoService;
import tip.java.sistemacentrocrecer.dto.PermisoRequestDTO;
import tip.java.sistemacentrocrecer.dto.PermisoResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permisos")
@AllArgsConstructor
@CrossOrigin("*")
public class PermisoController {
    private final PermisoService permisoService;

    @GetMapping
    public ResponseEntity<List<PermisoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(permisoService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<PermisoResponseDTO>> listarActivos() {
        return ResponseEntity.ok(permisoService.listarActivos());
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<PermisoResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(
                permisoService.obtenerPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<PermisoResponseDTO> crear(@Valid @RequestBody PermisoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(permisoService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermisoResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody PermisoRequestDTO dto) {
        return ResponseEntity.ok(permisoService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/baja")
    public ResponseEntity<Void>
    darDeBaja(@PathVariable Integer id) {
        permisoService.darDeBaja(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        permisoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/actividad/{actividadId}")
    public ResponseEntity<List<PermisoResponseDTO>> listarPorActividad(
            @PathVariable Integer actividadId) {
        return ResponseEntity.ok(permisoService.listarPorActividad(actividadId));
    }

    @PatchMapping("/{id}/autorizar")
    public ResponseEntity<PermisoResponseDTO> autorizar(@PathVariable Integer id) {
        return ResponseEntity.ok(permisoService.autorizar(id));
    }

    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<PermisoResponseDTO> rechazar(@PathVariable Integer id) {
        return ResponseEntity.ok(permisoService.rechazar(id));
    }

    @GetMapping("/responsable/{responsableId}")
    public ResponseEntity<List<PermisoResponseDTO>> listarPorResponsable(
            @PathVariable Integer responsableId) {
        return ResponseEntity.ok(permisoService.listarPorResponsable(responsableId));
    }
}
