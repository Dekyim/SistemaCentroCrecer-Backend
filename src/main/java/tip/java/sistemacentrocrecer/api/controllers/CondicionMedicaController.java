package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.CondicionMedicaService;
import tip.java.sistemacentrocrecer.dto.CondicionMedicaRequestDTO;
import tip.java.sistemacentrocrecer.dto.CondicionMedicaResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/condiciones-medicas")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CondicionMedicaController {
    private final CondicionMedicaService condicionMedicaService;

    @GetMapping
    public ResponseEntity<List<CondicionMedicaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(condicionMedicaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CondicionMedicaResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(condicionMedicaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CondicionMedicaResponseDTO> crear(@Valid @RequestBody CondicionMedicaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(condicionMedicaService.crear(dto));
    }

    @PutMapping("/{id}/actualizar")
    public ResponseEntity<CondicionMedicaResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody CondicionMedicaRequestDTO dto) {
        return ResponseEntity.ok(condicionMedicaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}/eliminar")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        condicionMedicaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
