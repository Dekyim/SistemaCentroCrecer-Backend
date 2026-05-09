package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.ReporteNinioService;
import tip.java.sistemacentrocrecer.dto.ReporteNinioRequestDTO;
import tip.java.sistemacentrocrecer.dto.ReporteNinioResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes-ninios")
@AllArgsConstructor
public class ReporteNinioController {
    private final ReporteNinioService reporteNinioService;

    @GetMapping
    public ResponseEntity<List<ReporteNinioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(reporteNinioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteNinioResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(reporteNinioService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ReporteNinioResponseDTO> crear(@Valid @RequestBody ReporteNinioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reporteNinioService.crear(dto));
    }

    @PutMapping("/{id}/actualizar")
    public ResponseEntity<ReporteNinioResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody ReporteNinioRequestDTO dto) {
        return ResponseEntity.ok(reporteNinioService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}/eliminar")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        reporteNinioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
