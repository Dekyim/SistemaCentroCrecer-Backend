package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.FuncionarioService;
import tip.java.sistemacentrocrecer.dto.FuncionarioResponseDTO;
import tip.java.sistemacentrocrecer.dto.FuncionarioRequestDTO;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/funcionarios")
@AllArgsConstructor
public class FuncionarioController {
    private final FuncionarioService funcionarioService;

    @GetMapping
    public List<FuncionarioResponseDTO> listarTodos() {
        return funcionarioService.listarTodos();
    }

    @GetMapping("/activos")
    public List<FuncionarioResponseDTO> listarActivos() {
        return funcionarioService.listarActivos();
    }

    @PostMapping
    public FuncionarioResponseDTO crear(@Valid @RequestBody FuncionarioRequestDTO dto) {
        return funcionarioService.crear(dto);
    }

    @PutMapping("/{id}")
    public FuncionarioResponseDTO actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody FuncionarioRequestDTO dto
    ) {
        return funcionarioService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void darDeBaja(@PathVariable Integer id) {
        funcionarioService.darDeBaja(id);
    }
}
