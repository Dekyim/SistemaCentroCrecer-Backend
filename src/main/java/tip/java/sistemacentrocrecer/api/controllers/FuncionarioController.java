package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.FuncionarioService;
import tip.java.sistemacentrocrecer.dto.CambiarContraseniaRequestDTO;
import tip.java.sistemacentrocrecer.dto.CambiarContraseniaResponseDTO;
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
    public FuncionarioResponseDTO actualizar(@PathVariable Integer id, @Valid @RequestBody FuncionarioRequestDTO dto) {
        return funcionarioService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void darDeBaja(@PathVariable Integer id) {
        funcionarioService.darDeBaja(id);
    }

    @PutMapping("/{id}/cambiar-contrasenia")
    public ResponseEntity<CambiarContraseniaResponseDTO> cambiarPassword(@PathVariable Integer id, @RequestBody CambiarContraseniaRequestDTO requestDTO) {
        CambiarContraseniaResponseDTO response = funcionarioService.cambiarPassword(id, requestDTO);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/alta")
    public ResponseEntity<Void> darDeAlta(@PathVariable Integer id) {
        funcionarioService.darDeAlta(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public FuncionarioResponseDTO obtenerPorId(@PathVariable Integer id) {
        return funcionarioService.obtenerPorId(id);
    }


}
