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
import java.util.Map;

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
    public ResponseEntity<AsistenciaResponseDTO> registrarMiSalida(@RequestBody Map<String, String> body) {
        LocalDate fecha = body.containsKey("fecha") ? LocalDate.parse(body.get("fecha")) : null;
        LocalTime horaSalida = body.containsKey("horaSalida") ? LocalTime.parse(body.get("horaSalida")) : null;
        return ResponseEntity.ok(asistenciaService.registrarSalidaPropia(fecha, horaSalida));
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
            @PathVariable Integer id, @RequestBody Map<String, String> body) {
        LocalTime horaSalida = body.containsKey("horaSalida") ? LocalTime.parse(body.get("horaSalida")) : null;
        return ResponseEntity.ok(asistenciaService.registrarSalidaNinio(id, horaSalida));
    }

    @GetMapping("/mis-ninios-disponibles")
    public ResponseEntity<List<NinioResponseDTO>> misNiniosDisponibles() {
        return ResponseEntity.ok(asistenciaService.listarNiniosDeMisGrupos());
    }
}