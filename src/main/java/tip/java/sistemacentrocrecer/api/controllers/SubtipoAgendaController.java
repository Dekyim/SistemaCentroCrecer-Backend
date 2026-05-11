package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.SubtipoAgendaService;
import tip.java.sistemacentrocrecer.dto.SubtipoAgendaRequestDTO;
import tip.java.sistemacentrocrecer.dto.SubtipoAgendaResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subtiposagendas")
@AllArgsConstructor
public class SubtipoAgendaController {

    private final SubtipoAgendaService subtipoAgendaService;

    @GetMapping
    public List<SubtipoAgendaResponseDTO> listarTodos() {
        return subtipoAgendaService.listarTodos();
    }

    @GetMapping("/{id}")
    public SubtipoAgendaResponseDTO obtenerPorId(@PathVariable Integer id) {
        return subtipoAgendaService.obtenerPorId(id);
    }

    @PostMapping
    public SubtipoAgendaResponseDTO crear(@Valid @RequestBody SubtipoAgendaRequestDTO dto) {
        return subtipoAgendaService.crear(dto);
    }

    @PutMapping("/{id}")
    public SubtipoAgendaResponseDTO actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody SubtipoAgendaRequestDTO dto
    ) {
        return subtipoAgendaService.actualizar(id, dto);
    }

}