package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.ActividadService;
import tip.java.sistemacentrocrecer.dto.ActividadRequestDTO;
import tip.java.sistemacentrocrecer.dto.ActividadResponseDTO;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/actividades")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ActividadController {
    private final ActividadService actividadService;

    @GetMapping
    public ResponseEntity<List<ActividadResponseDTO>> listarTodos() {
        return ResponseEntity.ok(actividadService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ActividadResponseDTO>> listarActivos() {
        return ResponseEntity.ok(
                actividadService.listarActivos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActividadResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(actividadService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ActividadResponseDTO> crear(@Valid @RequestBody ActividadRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(actividadService.crear(dto));
    }

    @PutMapping("/{id}/actualizar")
    public ResponseEntity<ActividadResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody ActividadRequestDTO dto) {
        return ResponseEntity.ok(actividadService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/baja")
    public ResponseEntity<Void> darDeBaja(@PathVariable Integer id) {
        actividadService.darDeBaja(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {actividadService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ninios")
    public ResponseEntity<ActividadResponseDTO> asignarNinios(@PathVariable Integer id, @RequestBody List<Integer> niniosIds) {
        return ResponseEntity.ok(actividadService.asignarNinios(id, niniosIds));
    }

    @GetMapping("/ninio/{ninioId}")
    public ResponseEntity<List<ActividadResponseDTO>> listarPorNinio(@PathVariable Integer ninioId) {
        return ResponseEntity.ok(actividadService.listarPorNinio(ninioId));
    }

    @GetMapping("/proximas")
    public ResponseEntity<List<ActividadResponseDTO>> listarProximas() {
        return ResponseEntity.ok(actividadService.listarProximas());
    }

    @GetMapping("/{id}/autorizacion")
    public ResponseEntity<Map<String, Object>> validarAutorizacion(@PathVariable Integer id) {
        return ResponseEntity.ok(actividadService.validarAutorizacion(id));
    }
}
