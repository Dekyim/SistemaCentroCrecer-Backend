package tip.java.sistemacentrocrecer.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.DashboardService;
import tip.java.sistemacentrocrecer.dto.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/admin/stats")
    public ResponseEntity<AdminStatsResponseDTO> getAdminStats() {

        return ResponseEntity.ok(dashboardService.getAdminStats());
    }

    @GetMapping("/funcionario/stats")
    public ResponseEntity<FuncionarioStatsResponseDTO> getFuncionarioStats() {

        return ResponseEntity.ok(dashboardService.getFuncionarioStats());
    }

    @GetMapping("/coordinacion")
    public ResponseEntity<CoordinacionDashboardResponseDTO> getCoordinacionDashboard() {
        return ResponseEntity.ok(dashboardService.getCoordinacionDashboard());
    }

    @GetMapping("/coordinacion/eventos-hoy")
    public ResponseEntity<List<AgendaResponseDTO>> getEventosDelDia(
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate fecha) {
        return ResponseEntity.ok(
                dashboardService.getEventosDelDia(
                        fecha != null ? fecha : LocalDate.now()
                )
        );
    }

    @GetMapping("/coordinacion/actividades-activas")
    public ResponseEntity<List<ActividadResponseDTO>> getActividadesActivas() {
        return ResponseEntity.ok(dashboardService.getActividadesActivas(LocalDate.now()));
    }

    @GetMapping("/coordinacion/conflictos")
    public ResponseEntity<List<AgendaResponseDTO>> getConflictos(@RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate fecha) {
        return ResponseEntity.ok(
                dashboardService.getConflictosAgenda(
                        fecha != null ? fecha : LocalDate.now()
                )
        );
    }

    @GetMapping("/coordinacion/limpiezas-pendientes")
    public ResponseEntity<List<AgendaLimpiezaResponseDTO>> getLimpiezasPendientes() {
        return ResponseEntity.ok(dashboardService.getLimpiezasPendientes());
    }

    @GetMapping("/coordinacion/actividades-proximas")
    public ResponseEntity<List<ActividadResponseDTO>> getActividadesProximas() {
        return ResponseEntity.ok(dashboardService.getActividadesProximas(LocalDate.now()));
    }
}
