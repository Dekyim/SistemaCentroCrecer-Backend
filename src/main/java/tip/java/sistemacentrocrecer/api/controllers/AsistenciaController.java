package tip.java.sistemacentrocrecer.api.controllers;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.AsistenciaService;
import tip.java.sistemacentrocrecer.dto.*;

import java.time.LocalDate;
import java.time.LocalTime;
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

    @PostMapping("/mi-entrada")
    public ResponseEntity<AsistenciaResponseDTO> registrarMiEntrada(@RequestBody RegistroEntradaFuncionarioRequestDTO dto) {
        return ResponseEntity.ok(asistenciaService.registrarEntradaPropia(dto));
    }

    @PutMapping("/mi-salida")
    public ResponseEntity<AsistenciaResponseDTO> registrarMiSalida(@RequestBody RegistroSalidaFuncionarioRequestDTO dto) {
        return ResponseEntity.ok(asistenciaService.registrarSalidaPropia(dto));
    }

    @GetMapping("/mi-registro")
    public ResponseEntity<AsistenciaResponseDTO> miRegistroDelDia(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        AsistenciaResponseDTO dto = asistenciaService.obtenerMiRegistroDelDia(fecha);
        if (dto == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/ninio")
    public ResponseEntity<AsistenciaResponseDTO> marcarAsistenciaNinio(@RequestBody AsistenciaNinioRequestDTO dto) {
        return ResponseEntity.ok(asistenciaService.marcarAsistenciaNinio(dto));
    }

    @GetMapping("/mis-ninios")
    public ResponseEntity<List<AsistenciaResponseDTO>> asistenciasDeNiniosPorFecha(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(asistenciaService.listarAsistenciasDeNinosPorFecha(fecha));
    }

    @PutMapping("/ninio/{id}/salida")
    public ResponseEntity<AsistenciaResponseDTO> registrarSalidaNinio(
            @PathVariable Integer id, @RequestBody RegistroSalidaNinioRequestDTO dto) {
        return ResponseEntity.ok(asistenciaService.registrarSalidaNinio(id, dto));
    }

    @GetMapping("/mis-ninios-disponibles")
    public ResponseEntity<List<NinioResponseDTO>> misNiniosDisponibles() {
        return ResponseEntity.ok(asistenciaService.listarNiniosDeMisGrupos());
    }

    @GetMapping("/historial/{cedula}")
    public ResponseEntity<List<AsistenciaResponseDTO>> historialPorCedula(@PathVariable String cedula) {
        return ResponseEntity.ok(asistenciaService.historialPorCedula(cedula));
    }

    @GetMapping("/frecuencia/{cedula}")
    public ResponseEntity<FrecuenciaAsistenciaResponseDTO> frecuenciaPorCedula(
            @PathVariable String cedula,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(asistenciaService.frecuenciaPorCedula(cedula, desde, hasta));
    }

    @GetMapping("/funcionarios/rango")
    public ResponseEntity<List<AsistenciaResponseDTO>> asistenciasFuncionariosPorRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(asistenciaService.listarAsistenciasFuncionariosPorRango(desde, hasta));
    }
}