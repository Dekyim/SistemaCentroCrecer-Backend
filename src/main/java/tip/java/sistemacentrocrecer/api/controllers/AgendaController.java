package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.AgendaService;
import tip.java.sistemacentrocrecer.dto.AgendaRequestDTO;
import tip.java.sistemacentrocrecer.dto.AgendaResponseDTO;

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
    public AgendaResponseDTO actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody AgendaRequestDTO dto
    ) {
        return agendaService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void darDeBaja(@PathVariable Integer id) {
        agendaService.darDeBaja(id);
    }
    @GetMapping("/test")
    public String test() {
        return "OK";
    }
}