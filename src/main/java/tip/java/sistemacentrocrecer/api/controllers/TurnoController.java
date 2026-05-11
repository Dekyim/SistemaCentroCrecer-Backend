package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.TurnoService;
import tip.java.sistemacentrocrecer.dto.TurnoRequestDTO;
import tip.java.sistemacentrocrecer.dto.TurnoResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/turnos")
@AllArgsConstructor
public class TurnoController {

    private final TurnoService turnoService;

    @GetMapping
    public List<TurnoResponseDTO> listarTodos() {
        return turnoService.listarTodos();
    }

    @GetMapping("/activos")
    public List<TurnoResponseDTO> listarActivos() {
        return turnoService.listarActivos();
    }

    @GetMapping("/{id}")
    public TurnoResponseDTO obtenerPorId(@PathVariable Integer id) {
        return turnoService.obtenerPorId(id);
    }

    @PostMapping
    public TurnoResponseDTO crear(@Valid @RequestBody TurnoRequestDTO dto) {
        return turnoService.crear(dto);
    }

    @PutMapping("/{id}")
    public TurnoResponseDTO actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody TurnoRequestDTO dto
    ) {
        return turnoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void darDeBaja(@PathVariable Integer id) {
        turnoService.darDeBaja(id);
    }
}