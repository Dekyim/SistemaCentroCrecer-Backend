package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.EmpresaExternaService;
import tip.java.sistemacentrocrecer.dto.EmpresaExternaRequestDTO;
import tip.java.sistemacentrocrecer.dto.EmpresaExternaResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/empresas-externas")
@AllArgsConstructor
@CrossOrigin("*")
public class EmpresaExternaController {
    private final EmpresaExternaService empresaExternaService;

    @GetMapping
    public ResponseEntity<List<EmpresaExternaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(empresaExternaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaExternaResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(empresaExternaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EmpresaExternaResponseDTO> crear(@Valid @RequestBody EmpresaExternaRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(empresaExternaService.crear(dto));
    }

    @PutMapping("/{id}/actualizar")
    public ResponseEntity<EmpresaExternaResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody EmpresaExternaRequestDTO dto) {
        return ResponseEntity.ok(empresaExternaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}/eliminar")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        empresaExternaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/actividad/{actividadId}")
    public ResponseEntity<List<EmpresaExternaResponseDTO>> listarPorActividad(@PathVariable Integer actividadId) {
        return ResponseEntity.ok(empresaExternaService.listarPorActividad(actividadId));
    }

    @GetMapping("/tipo/{tipoServicio}")
    public ResponseEntity<List<EmpresaExternaResponseDTO>> listarPorTipo(@PathVariable String tipoServicio) {
        return ResponseEntity.ok(empresaExternaService.listarPorTipoServicio(tipoServicio));
    }

    @PatchMapping("/{id}/desasignar")
    public ResponseEntity<EmpresaExternaResponseDTO> desasignarActividad(@PathVariable Integer id) {
        return ResponseEntity.ok(empresaExternaService.desasignarActividad(id));
    }

}
