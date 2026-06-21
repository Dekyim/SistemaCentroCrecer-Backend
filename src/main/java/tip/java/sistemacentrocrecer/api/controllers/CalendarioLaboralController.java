package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.CalendarioLaboralService;
import tip.java.sistemacentrocrecer.dto.DiaNoLaborableRequestDTO;
import tip.java.sistemacentrocrecer.dto.DiaNoLaborableResponseDTO;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/calendario-laboral")
@RequiredArgsConstructor
public class CalendarioLaboralController {

    private static final String SOLO_ADMIN =
            "hasAnyRole('ADMIN','ADMINISTRADOR_SISTEMA')";

    private static final String ROLES_FUNCIONARIO =
            "hasAnyRole('ADMIN','ADMINISTRADOR_SISTEMA','COORDINADORA'," +
                    "'ASISTENTE_SOCIAL','PSICOLOGO','PSICOMOTRICISTA','MAESTRA'," +
                    "'ADMINISTRATIVO','EDUCADOR','TALLERISTA_PLASTICA'," +
                    "'TALLERISTA_CERAMICA','TALLERISTA_CORPORAL','AUXILIAR_LIMPIEZA')";

    private final CalendarioLaboralService calendarioLaboralService;

    @GetMapping
    @PreAuthorize(ROLES_FUNCIONARIO)
    public ResponseEntity<List<DiaNoLaborableResponseDTO>> listar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(calendarioLaboralService.listar(desde, hasta));
    }

    @PostMapping
    @PreAuthorize(SOLO_ADMIN)
    public ResponseEntity<DiaNoLaborableResponseDTO> crear(
            @Valid @RequestBody DiaNoLaborableRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(calendarioLaboralService.crear(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize(SOLO_ADMIN)
    public ResponseEntity<DiaNoLaborableResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody DiaNoLaborableRequestDTO dto) {
        return ResponseEntity.ok(calendarioLaboralService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(SOLO_ADMIN)
    public ResponseEntity<Void> darDeBaja(@PathVariable Integer id) {
        calendarioLaboralService.darDeBaja(id);
        return ResponseEntity.noContent().build();
    }
}
