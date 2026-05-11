package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.DetalleAgendaService;
import tip.java.sistemacentrocrecer.dto.DetalleAgendaRequestDTO;
import tip.java.sistemacentrocrecer.dto.DetalleAgendaResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/detallesagendas")
@AllArgsConstructor
public class DetalleAgendaController {

    private final DetalleAgendaService detalleAgendaService;

    @GetMapping
    public List<DetalleAgendaResponseDTO> listarTodos() {
        return detalleAgendaService.listarTodos();
    }

    @GetMapping("/activos")
    public List<DetalleAgendaResponseDTO> listarActivos() {
        return detalleAgendaService.listarActivos();
    }

    @GetMapping("/{id}")
    public DetalleAgendaResponseDTO obtenerPorId(@PathVariable Integer id) {
        return detalleAgendaService.obtenerPorId(id);
    }

    @PostMapping
    public DetalleAgendaResponseDTO crear(@Valid @RequestBody DetalleAgendaRequestDTO dto) {
        return detalleAgendaService.crear(dto);
    }

    @PutMapping("/{id}")
    public DetalleAgendaResponseDTO actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody DetalleAgendaRequestDTO dto
    ) {
        return detalleAgendaService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void darDeBaja(@PathVariable Integer id) {
        detalleAgendaService.darDeBaja(id);
    }

}