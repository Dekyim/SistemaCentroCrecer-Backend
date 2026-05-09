package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.ReporteService;
import tip.java.sistemacentrocrecer.dto.ReporteRequestDTO;
import tip.java.sistemacentrocrecer.dto.ReporteResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
@AllArgsConstructor
public class ReporteController {
    private final ReporteService reporteService;

    @PostMapping
    public ResponseEntity<ReporteResponseDTO> crear(@RequestBody ReporteRequestDTO dto) {
        return ResponseEntity.status(201).body(reporteService.crearReporte(dto));
    }

    @GetMapping
    public ResponseEntity<List<ReporteResponseDTO>> listarTodos() {
        return ResponseEntity.ok(reporteService.listarTodos());
    }

    @GetMapping("/activos")
    public List<ReporteResponseDTO> listarActivos() {
        return reporteService.listarActivos();
    }

    @PutMapping("/{id}/actualizar")
    public ResponseEntity<ReporteResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody ReporteRequestDTO dto) {
        return ResponseEntity.ok(reporteService.actualizarReporte(id, dto));
    }

    @PutMapping("/{id}/baja")
    public ResponseEntity<Void> darDeBaja(@PathVariable Integer id) {
        reporteService.darDeBaja(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ReporteResponseDTO obtenerPorId(@PathVariable Integer id) {
        return reporteService.obtenerPorId(id);
    }




}
