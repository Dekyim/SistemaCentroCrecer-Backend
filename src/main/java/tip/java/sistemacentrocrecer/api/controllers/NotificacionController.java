package tip.java.sistemacentrocrecer.api.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.NotificacionService;
import tip.java.sistemacentrocrecer.dto.NotificacionResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notificaciones")
@AllArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @GetMapping("/funcionario/{funcionarioId}")
    public ResponseEntity<List<NotificacionResponseDTO>> listar(@PathVariable Integer funcionarioId) {
        return ResponseEntity.ok(notificacionService.listarPorFuncionario(funcionarioId));
    }

    @GetMapping("/funcionario/{funcionarioId}/no-leidas")
    public ResponseEntity<Long> contarNoLeidas(@PathVariable Integer funcionarioId) {
        return ResponseEntity.ok(notificacionService.contarNoLeidas(funcionarioId));
    }

    @PutMapping("/{id}/leida")
    public ResponseEntity<Void> marcarLeida(@PathVariable Integer id) {
        notificacionService.marcarComoLeida(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/funcionario/{funcionarioId}/leer-todas")
    public ResponseEntity<Void> marcarTodasLeidas(@PathVariable Integer funcionarioId) {
        notificacionService.marcarTodasComoLeidas(funcionarioId);
        return ResponseEntity.ok().build();
    }
}