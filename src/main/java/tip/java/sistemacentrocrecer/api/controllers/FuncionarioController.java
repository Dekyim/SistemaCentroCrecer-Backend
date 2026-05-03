package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.FuncionarioService;
import tip.java.sistemacentrocrecer.dto.FuncionarioResponseDTO;
import tip.java.sistemacentrocrecer.dto.FuncionarioRequestDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/funcionarios")
@AllArgsConstructor
public class FuncionarioController {
    private final FuncionarioService funcionarioService;

    @GetMapping
    public List<FuncionarioRequestDTO> listarTodos() {
        return funcionarioService.listarTodos();
    }

    @GetMapping("/activos")
    public List<FuncionarioRequestDTO> listarActivos() {
        return funcionarioService.listarActivos();
    }

    @PostMapping
    public FuncionarioRequestDTO crear(@Valid @RequestBody FuncionarioResponseDTO dto) {
        return funcionarioService.crear(dto);
    }

    @PutMapping("/{id}")
    public FuncionarioRequestDTO actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody FuncionarioResponseDTO dto
    ) {
        return funcionarioService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void darDeBaja(@PathVariable Integer id) {
        funcionarioService.darDeBaja(id);
    }
}
