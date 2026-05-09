package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.ReporteGrupoService;
import tip.java.sistemacentrocrecer.dto.ReporteGrupoRequestDTO;
import tip.java.sistemacentrocrecer.dto.ReporteGrupoResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes-grupos")
@AllArgsConstructor
public class ReporteGrupoController {
    private final ReporteGrupoService reporteGrupoService;

    @GetMapping
    public ResponseEntity<List<ReporteGrupoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(reporteGrupoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteGrupoResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(reporteGrupoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ReporteGrupoResponseDTO> crear(@Valid @RequestBody ReporteGrupoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reporteGrupoService.crear(dto));
    }

    @PutMapping("/{id}/actualizar")
    public ResponseEntity<ReporteGrupoResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody ReporteGrupoRequestDTO dto) {
        return ResponseEntity.ok(reporteGrupoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}/eliminar")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        reporteGrupoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


}
