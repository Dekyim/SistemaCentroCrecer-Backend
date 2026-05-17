package tip.java.sistemacentrocrecer.biz.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.repositories.*;
import tip.java.sistemacentrocrecer.dto.AdminStatsResponseDTO;
import tip.java.sistemacentrocrecer.dto.FuncionarioStatsResponseDTO;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final FuncionarioRepository funcionarioRepository;
    private final NinioRepository ninioRepository;
    private final ActividadRepository actividadRepository;
    private final TurnoRepository turnoRepository;
    private final GrupoRepository grupoRepository;

    public AdminStatsResponseDTO getAdminStats() {

        long funcionariosActivos = funcionarioRepository.countByActivoTrue();

        long funcionariosTotales = funcionarioRepository.count();

        long niniosTotales = ninioRepository.count();

        long actividadesTotales = actividadRepository.count();

        long turnosActivos = turnoRepository.countByActivoTrue();

        long gruposActivos = grupoRepository.countByActivoTrue();

        int coberturaPorcentaje = calcularCobertura(funcionariosActivos);

        return new AdminStatsResponseDTO(
                funcionariosActivos,
                funcionariosTotales,
                niniosTotales,
                actividadesTotales,
                turnosActivos,
                gruposActivos,
                coberturaPorcentaje
        );
    }

    public FuncionarioStatsResponseDTO getFuncionarioStats() {

        long niniosTotales = ninioRepository.count();

        long actividadesTotales = actividadRepository.count();

        long gruposActivos = grupoRepository.countByActivoTrue();

        return new FuncionarioStatsResponseDTO(
                niniosTotales,
                actividadesTotales,
                gruposActivos
        );
    }

    private int calcularCobertura(long funcionariosActivos) {

        final int FULL_STAFF = 15;
        return (int) Math.min(100, (funcionariosActivos * 100L) / FULL_STAFF);
    }
}
