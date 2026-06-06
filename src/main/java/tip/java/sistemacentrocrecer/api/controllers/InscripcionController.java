package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.InscripcionService;
import tip.java.sistemacentrocrecer.dto.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inscripciones")
@RequiredArgsConstructor
@CrossOrigin("*")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    private static final String ROLES_FUNCIONARIO =
            "hasAnyRole('ADMIN','ADMINISTRADOR_SISTEMA'," +
                    "'COORDINADORA','ASISTENTE_SOCIAL','PSICOLOGO','PSICOMOTRICISTA'," +
                    "'MAESTRA','ADMINISTRATIVO','EDUCADOR'," +
                    "'TALLERISTA_PLASTICA','TALLERISTA_CERAMICA','TALLERISTA_CORPORAL'," +
                    "'AUXILIAR_LIMPIEZA')";

    private static final String ROLES_GESTION =
            "hasAnyRole('ADMIN','ADMINISTRADOR_SISTEMA'," +
                    "'COORDINADORA','ASISTENTE_SOCIAL','PSICOLOGO')";

    @GetMapping
    @PreAuthorize(ROLES_FUNCIONARIO)
    public ResponseEntity<List<InscripcionResponseDTO>> listarTodos() {
        return ResponseEntity.ok(inscripcionService.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize(ROLES_FUNCIONARIO)
    public ResponseEntity<InscripcionResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(inscripcionService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize(ROLES_GESTION)
    public ResponseEntity<InscripcionResponseDTO> crear(@Valid @RequestBody InscripcionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inscripcionService.crear(dto));
    }

    @PutMapping("/{id}/actualizar")
    @PreAuthorize(ROLES_GESTION)
    public ResponseEntity<InscripcionResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody InscripcionRequestDTO dto) {
        return ResponseEntity.ok(inscripcionService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}/eliminar")
    @PreAuthorize("hasAnyRole('ADMIN','ADMINISTRADOR_SISTEMA')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        inscripcionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pendientes")
    @PreAuthorize(ROLES_GESTION)
    public ResponseEntity<List<InscripcionSolicitudResponseDTO>> listarPendientes() {
        return ResponseEntity.ok(inscripcionService.listarPendientes());
    }

    @PutMapping("/{id}/dar-de-alta")
    @PreAuthorize(ROLES_GESTION)
    public ResponseEntity<InscripcionSolicitudResponseDTO> darDeAlta(@PathVariable Integer id, @Valid @RequestBody DarDeAltaRequestDTO dto) {
        return ResponseEntity.ok(inscripcionService.darDeAlta(id, dto));
    }

    @PutMapping("/{id}/rechazar")
    @PreAuthorize(ROLES_GESTION)
    public ResponseEntity<Void> rechazar(@PathVariable Integer id, @Valid @RequestBody RechazarInscripcionRequestDTO dto) {
        inscripcionService.rechazar(id, dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/por-responsable/{responsableId}")
    @PreAuthorize("hasRole('RESPONSABLE')")
    public ResponseEntity<List<InscripcionSolicitudResponseDTO>> listarPorResponsable(
            @PathVariable Integer responsableId) {
        return ResponseEntity.ok(inscripcionService.listarPorResponsable(responsableId));
    }

    @PostMapping("/responsable/{responsableId}/solicitar")
    @PreAuthorize("hasRole('RESPONSABLE')")
    public ResponseEntity<List<InscripcionSolicitudResponseDTO>> solicitarNuevosNinos(
            @PathVariable Integer responsableId,
            @Valid @RequestBody java.util.List<tip.java.sistemacentrocrecer.dto.RegistroCompletoRequestDTO.NinioSolicitudDTO> ninos) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inscripcionService.solicitarNuevosNinos(responsableId, ninos));
    }
}