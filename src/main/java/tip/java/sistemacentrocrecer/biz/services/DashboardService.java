package tip.java.sistemacentrocrecer.biz.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.entities.Agenda;
import tip.java.sistemacentrocrecer.biz.dao.enums.EstadoLimpiezaEnum;
import tip.java.sistemacentrocrecer.biz.dao.repositories.*;
import tip.java.sistemacentrocrecer.dto.*;
import tip.java.sistemacentrocrecer.mapper.ActividadMapper;
import tip.java.sistemacentrocrecer.mapper.AgendaLimpiezaMapper;
import tip.java.sistemacentrocrecer.mapper.AgendaMapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final FuncionarioRepository funcionarioRepository;
    private final NinioRepository ninioRepository;
    private final ActividadRepository actividadRepository;
    private final TurnoRepository turnoRepository;
    private final GrupoRepository grupoRepository;
    private final AgendaRepository         agendaRepository;
    private final AgendaLimpiezaRepository  agendaLimpiezaRepository;
    private final AgendaMapper agendaMapper;
    private final ActividadMapper actividadMapper;
    private final AgendaLimpiezaMapper agendaLimpiezaMapper;

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

    public CoordinacionDashboardResponseDTO getCoordinacionDashboard() {
        LocalDate hoy = LocalDate.now();

        List<AgendaResponseDTO> eventosHoy = getEventosDelDia(hoy);
        List<ActividadResponseDTO> actActivas = getActividadesActivas(hoy);
        List<AgendaResponseDTO> conflictos = getConflictosAgenda(hoy);
        List<AgendaLimpiezaResponseDTO> limpPendientes = getLimpiezasPendientes();
        List<ActividadResponseDTO> actProximas = getActividadesProximas(hoy);

        return CoordinacionDashboardResponseDTO.builder()
                .eventosDelDia(eventosHoy)
                .totalEventosDelDia(eventosHoy.size())
                .actividadesActivas(actActivas)
                .totalActividadesActivas(actActivas.size())
                .conflictosAgenda(conflictos)
                .totalConflictos(conflictos.size())
                .limpiezasPendientes(limpPendientes)
                .totalLimpiezasPendientes(limpPendientes.size())
                .actividadesProximas(actProximas)
                .totalActividadesProximas(actProximas.size())
                .build();
    }

    public List<AgendaResponseDTO> getEventosDelDia(LocalDate fecha) {
        return agendaRepository.findByFecha(fecha)
                .stream()
                .filter(Agenda::getActivo)
                .map(agendaMapper::toResponseDTO)
                .toList();
    }

    public List<ActividadResponseDTO> getActividadesActivas(LocalDate hoy) {
        return actividadRepository
                .findByActivoTrueAndFechaDesdeLessThanEqualAndFechaHastaGreaterThanEqual(hoy, hoy)
                .stream()
                .map(actividadMapper::toResponseDTO)
                .toList();
    }

    public List<AgendaResponseDTO> getConflictosAgenda(LocalDate fecha) {
        return agendaRepository.findByFecha(fecha)
                .stream()
                .filter(Agenda::getActivo)
                .collect(Collectors.groupingBy(
                        a -> a.getFuncionario().getId() + "-"
                                + a.getHoraInicio() + "-"
                                + a.getHoraFin()
                ))
                .values().stream()
                .filter(lista -> lista.size() > 1)
                .flatMap(Collection::stream)
                .map(agendaMapper::toResponseDTO)
                .toList();
    }

    public List<AgendaLimpiezaResponseDTO> getLimpiezasPendientes() {
        return agendaLimpiezaRepository
                .findByEstado(EstadoLimpiezaEnum.PENDIENTE)
                .stream()
                .map(agendaLimpiezaMapper::toResponseDTO)
                .toList();
    }

    public List<ActividadResponseDTO> getActividadesProximas(LocalDate hoy) {
        return actividadRepository
                .findByActivoTrueAndFechaDesdeGreaterThan(hoy)
                .stream()
                .map(actividadMapper::toResponseDTO)
                .toList();
    }

    private int calcularCobertura(long funcionariosActivos) {

        final int FULL_STAFF = 15;
        return (int) Math.min(100, (funcionariosActivos * 100L) / FULL_STAFF);
    }
}
