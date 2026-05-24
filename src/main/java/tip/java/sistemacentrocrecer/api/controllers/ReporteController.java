package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    @GetMapping("/{id}")
    public ResponseEntity<ReporteResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(reporteService.obtenerPorId(id));
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        reporteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/excel")
    public ResponseEntity<byte[]> exportarExcel(@PathVariable Integer id) {
        byte[] excel = reporteService.exportarExcel(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reporte_" + id + ".xlsx\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excel);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> exportarPdf(@PathVariable Integer id) {
        byte[] pdf = reporteService.exportarPdf(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reporte_" + id + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}