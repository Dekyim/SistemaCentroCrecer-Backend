package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.ResponsableNinioService;
import tip.java.sistemacentrocrecer.dto.ResponsableNinioRequestDTO;
import tip.java.sistemacentrocrecer.dto.ResponsableNinioResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/responsables-ninios")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ResponsableNinioController {
    private final ResponsableNinioService responsableNinioService;

    @GetMapping
    public ResponseEntity<List<ResponsableNinioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(responsableNinioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsableNinioResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(responsableNinioService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ResponsableNinioResponseDTO> crear(@Valid @RequestBody ResponsableNinioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(responsableNinioService.crear(dto));
    }

    @PutMapping("/{id}/actualizar")
    public ResponseEntity<ResponsableNinioResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody ResponsableNinioRequestDTO dto) {
        return ResponseEntity.ok(responsableNinioService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}/eliminar")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        responsableNinioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
