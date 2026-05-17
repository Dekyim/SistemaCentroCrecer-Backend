package tip.java.sistemacentrocrecer.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tip.java.sistemacentrocrecer.biz.services.DashboardService;
import tip.java.sistemacentrocrecer.dto.AdminStatsResponseDTO;
import tip.java.sistemacentrocrecer.dto.FuncionarioStatsResponseDTO;

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
}
