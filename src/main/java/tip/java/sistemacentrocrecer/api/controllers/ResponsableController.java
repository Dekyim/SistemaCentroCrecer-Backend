package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.ResponsableService;
import tip.java.sistemacentrocrecer.dto.ResponsableRequestDTO;
import tip.java.sistemacentrocrecer.dto.ResponsableResponseDTO;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/responsables")
@RequiredArgsConstructor
public class ResponsableController {
    private final ResponsableService responsableService;

    @GetMapping
    public ResponseEntity<List<ResponsableResponseDTO>> listar() {
        return ResponseEntity.ok(responsableService.listar());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ResponsableResponseDTO>> listarActivos() {
        return ResponseEntity.ok(responsableService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsableResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(responsableService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ResponsableResponseDTO> crear(
            @Valid @RequestBody ResponsableRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responsableService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsableResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ResponsableRequestDTO dto) {

        return ResponseEntity.ok(responsableService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> bajaLogica(@PathVariable Integer id) {

        responsableService.bajaLogica(id);

        return ResponseEntity.noContent().build();
    }
}
