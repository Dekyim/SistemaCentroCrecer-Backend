package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.RolService;
import tip.java.sistemacentrocrecer.dto.RolRequestDTO;
import tip.java.sistemacentrocrecer.dto.RolResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@AllArgsConstructor
public class RolController {
    private final RolService rolService;

    @GetMapping
    public List<RolRequestDTO> listarTodos() {
        return rolService.listarTodos();
    }

    @GetMapping("/activos")
    public List<RolRequestDTO> listarActivos() {
        return rolService.listarActivos();
    }

    @PostMapping
    public RolRequestDTO crear(@Valid @RequestBody RolResponseDTO dto) {
        return rolService.crear(dto);
    }

    @PutMapping("/{id}")
    public RolRequestDTO actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody RolResponseDTO dto
    ) {
        return rolService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void darDeBaja(@PathVariable Integer id) {
        rolService.darDeBaja(id);
    }


}
