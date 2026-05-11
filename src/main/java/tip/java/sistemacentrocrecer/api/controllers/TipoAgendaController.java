package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.TipoAgendaService;
import tip.java.sistemacentrocrecer.dto.TipoAgendaRequestDTO;
import tip.java.sistemacentrocrecer.dto.TipoAgendaResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tiposagendas")
@AllArgsConstructor
public class TipoAgendaController {

    private final TipoAgendaService tipoAgendaService;

    @GetMapping
    public List<TipoAgendaResponseDTO> listarTodos() {
        return tipoAgendaService.listarTodos();
    }

    @GetMapping("/{id}")
    public TipoAgendaResponseDTO obtenerPorId(@PathVariable Integer id) {
        return tipoAgendaService.obtenerPorId(id);
    }

    @PostMapping
    public TipoAgendaResponseDTO crear(@Valid @RequestBody TipoAgendaRequestDTO dto) {
        return tipoAgendaService.crear(dto);
    }

    @PutMapping("/{id}")
    public TipoAgendaResponseDTO actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody TipoAgendaRequestDTO dto
    ) {
        return tipoAgendaService.actualizar(id, dto);
    }

}