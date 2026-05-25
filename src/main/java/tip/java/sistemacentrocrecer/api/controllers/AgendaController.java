package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.AgendaService;
import tip.java.sistemacentrocrecer.dto.AgendaFilterRequestDTO;
import tip.java.sistemacentrocrecer.dto.AgendaRequestDTO;
import tip.java.sistemacentrocrecer.dto.AgendaResponseDTO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/agendas")
@AllArgsConstructor
public class AgendaController {

    private final AgendaService agendaService;

    @GetMapping
    public List<AgendaResponseDTO> listarTodos() {
        return agendaService.listarTodos();
    }

    @GetMapping("/activos")
    public List<AgendaResponseDTO> listarActivos() {
        return agendaService.listarActivos();
    }

    @GetMapping("/{id}")
    public AgendaResponseDTO obtenerPorId(@PathVariable Integer id) {
        return agendaService.obtenerPorId(id);
    }

    @PostMapping
    public AgendaResponseDTO crear(@Valid @RequestBody AgendaRequestDTO dto) {
        return agendaService.crear(dto);
    }

    @PutMapping("/{id}")
    public AgendaResponseDTO actualizar(@PathVariable Integer id, @Valid @RequestBody AgendaRequestDTO dto) {return agendaService.actualizar(id, dto);}

    @DeleteMapping("/{id}")
    public void darDeBaja(@PathVariable Integer id) {
        agendaService.darDeBaja(id);
    }

    @PatchMapping("/{id}/alta")
    public AgendaResponseDTO darDeAlta(@PathVariable Integer id) {return agendaService.darDeAlta(id);}

    @GetMapping("/filtrar")
    public List<AgendaResponseDTO> filtrar(@Valid AgendaFilterRequestDTO filtro) {return agendaService.filtrar(filtro);}

    @GetMapping("/semana")
    public ResponseEntity<List<AgendaResponseDTO>> eventosSemana(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        LocalDate lunes = fecha.with(DayOfWeek.MONDAY);
        LocalDate domingo = fecha.with(DayOfWeek.SUNDAY);
        AgendaFilterRequestDTO filtro = new AgendaFilterRequestDTO();
        filtro.setFechaDesde(lunes);
        filtro.setFechaHasta(domingo);
        return ResponseEntity.ok(agendaService.filtrar(filtro));
    }
}