package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.DocumentoAdjuntoService;
import tip.java.sistemacentrocrecer.dto.DocumentoAdjuntoRequestDTO;
import tip.java.sistemacentrocrecer.dto.DocumentoAdjuntoResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documentosadjuntos")
@AllArgsConstructor
public class DocumentoAdjuntoController {

    private final DocumentoAdjuntoService documentoAdjuntoService;

    @GetMapping
    public List<DocumentoAdjuntoResponseDTO> listarTodos() {
        return documentoAdjuntoService.listarTodos();
    }

    @GetMapping("/{id}")
    public DocumentoAdjuntoResponseDTO obtenerPorId(@PathVariable Integer id) {
        return documentoAdjuntoService.obtenerPorId(id);
    }

    @PostMapping
    public DocumentoAdjuntoResponseDTO crear(@Valid @RequestBody DocumentoAdjuntoRequestDTO dto) {
        return documentoAdjuntoService.crear(dto);
    }

    @PutMapping("/{id}")
    public DocumentoAdjuntoResponseDTO actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody DocumentoAdjuntoRequestDTO dto
    ) {
        return documentoAdjuntoService.actualizar(id, dto);
    }

}