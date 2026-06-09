package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.InscripcionService;
import tip.java.sistemacentrocrecer.biz.services.ResponsableService;
import tip.java.sistemacentrocrecer.dto.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/responsables")
@RequiredArgsConstructor
public class ResponsableController {

    private static final String ROLES_FUNCIONARIO =
            "hasAnyRole('ADMIN','ADMINISTRADOR_SISTEMA'," +
                    "'COORDINADORA','ASISTENTE_SOCIAL','PSICOLOGO','PSICOMOTRICISTA'," +
                    "'MAESTRA','ADMINISTRATIVO','EDUCADOR'," +
                    "'TALLERISTA_PLASTICA','TALLERISTA_CERAMICA','TALLERISTA_CORPORAL'," +
                    "'AUXILIAR_LIMPIEZA')";

    private final ResponsableService  responsableService;
    private final InscripcionService  inscripcionService;

    @GetMapping
    @PreAuthorize(ROLES_FUNCIONARIO)
    public ResponseEntity<List<ResponsableResponseDTO>> listar() {
        return ResponseEntity.ok(responsableService.listar());
    }

    @GetMapping("/activos")
    @PreAuthorize(ROLES_FUNCIONARIO)
    public ResponseEntity<List<ResponsableResponseDTO>> listarActivos() {
        return ResponseEntity.ok(responsableService.listarActivos());
    }

    @GetMapping("/{id}")
    @PreAuthorize(ROLES_FUNCIONARIO + " or hasRole('RESPONSABLE')")
    public ResponseEntity<ResponsableResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(responsableService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponsableResponseDTO> crear(@Valid @RequestBody ResponsableRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(responsableService.crear(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<ResponsableResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody ResponsableRequestDTO dto) {
        return ResponseEntity.ok(responsableService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> bajaLogica(@PathVariable Integer id) {
        responsableService.bajaLogica(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/cambiar-contrasenia")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE')")
    public ResponseEntity<CambiarContraseniaResponseDTO> cambiarPassword(@PathVariable Integer id, @RequestBody CambiarContraseniaRequestDTO requestDTO) {
        return ResponseEntity.ok(responsableService.cambiarPassword(id, requestDTO));
    }

    @PutMapping("/{id}/perfil")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE')")
    public ResponseEntity<ResponsableResponseDTO> actualizarPerfil(@PathVariable Integer id, @Valid @RequestBody ActualizarPerfilRequestDTO dto) {
        return ResponseEntity.ok(responsableService.actualizarPerfil(id, dto));
    }

    @PutMapping("/{id}/cambiar-contrasenia-seguro")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE')")
    public ResponseEntity<CambiarContraseniaResponseDTO> cambiarPasswordSeguro(@PathVariable Integer id, @RequestBody CambiarContraseniaSeguraRequestDTO dto) {
        return ResponseEntity.ok(responsableService.cambiarPasswordSeguro(id, dto));
    }


    @PostMapping("/registro-completo")
    public ResponseEntity<List<InscripcionSolicitudResponseDTO>> registroCompleto(@Valid @RequestBody RegistroCompletoRequestDTO dto) {
        List<InscripcionSolicitudResponseDTO> solicitudes = inscripcionService.registrarResponsableConNinos(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitudes);
    }
}