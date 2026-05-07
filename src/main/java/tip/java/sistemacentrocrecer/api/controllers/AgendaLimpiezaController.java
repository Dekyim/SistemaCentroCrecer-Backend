package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.dao.enums.EstadoLimpiezaEnum;
import tip.java.sistemacentrocrecer.biz.services.AgendaLimpiezaService;
import tip.java.sistemacentrocrecer.biz.services.AgendaService;
import tip.java.sistemacentrocrecer.dto.AgendaLimpiezaRequestDTO;
import tip.java.sistemacentrocrecer.dto.AgendaLimpiezaResponseDTO;
import tip.java.sistemacentrocrecer.dto.AgendaRequestDTO;
import tip.java.sistemacentrocrecer.dto.AgendaResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agendaslimpiezas")
@AllArgsConstructor
public class AgendaLimpiezaController {

    private final AgendaLimpiezaService agendaLimpiezaService;

    @GetMapping
    public List<AgendaLimpiezaResponseDTO> listarTodos() {
        return agendaLimpiezaService.listarTodos();
    }

    @GetMapping("/estado/{estado}")
    public List<AgendaLimpiezaResponseDTO> listarPorEstado(@PathVariable EstadoLimpiezaEnum estado) {
        return agendaLimpiezaService.listarPorEstado(estado);
    }

    @GetMapping("/{id}")
    public AgendaLimpiezaResponseDTO obtenerPorId(@PathVariable Integer id) {
        return agendaLimpiezaService.obtenerPorId(id);
    }

    @PostMapping
    public AgendaLimpiezaResponseDTO crear(@Valid @RequestBody AgendaLimpiezaRequestDTO dto) {
        return agendaLimpiezaService.crear(dto);
    }

    @PutMapping("/{id}")
    public AgendaLimpiezaResponseDTO actualizar(@PathVariable Integer id, @Valid @RequestBody AgendaLimpiezaRequestDTO dto) {
        return agendaLimpiezaService.actualizar(id, dto);
    }

   @PatchMapping("/{id}/estado")
   public AgendaLimpiezaResponseDTO cambiarEstado(@PathVariable Integer id, @RequestParam EstadoLimpiezaEnum estado){
        return agendaLimpiezaService.cambiarEstado(id, estado);
   }
}