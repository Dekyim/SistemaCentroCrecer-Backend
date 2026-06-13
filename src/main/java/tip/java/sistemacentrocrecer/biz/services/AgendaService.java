package tip.java.sistemacentrocrecer.biz.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.entities.Agenda;
import tip.java.sistemacentrocrecer.biz.dao.entities.Funcionario;
import tip.java.sistemacentrocrecer.biz.dao.entities.TipoAgenda;
import tip.java.sistemacentrocrecer.biz.dao.repositories.AgendaRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.FuncionarioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.TipoAgendaRepository;
import tip.java.sistemacentrocrecer.dto.AgendaFilterRequestDTO;
import tip.java.sistemacentrocrecer.dto.AgendaRequestDTO;
import tip.java.sistemacentrocrecer.dto.AgendaResponseDTO;
import tip.java.sistemacentrocrecer.exceptions.BusinessException;
import tip.java.sistemacentrocrecer.exceptions.ResourceNotFoundException;
import tip.java.sistemacentrocrecer.mapper.AgendaMapper;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final TipoAgendaRepository tipoAgendaRepository;
    private final AgendaMapper agendaMapper;

    public List<AgendaResponseDTO> listarTodos() {
        return agendaRepository.findAll()
                .stream()
                .map(agendaMapper::toResponseDTO)
                .toList();
    }

    public List<AgendaResponseDTO> listarActivos() {
        return agendaRepository.findByActivoTrue()
                .stream()
                .map(agendaMapper::toResponseDTO)
                .toList();
    }

    public AgendaResponseDTO obtenerPorId(Integer id) {
        Agenda agenda = agendaRepository.findById(id).orElseThrow(() -> new RuntimeException("Agenda no encontrada con id: " + id));
        return agendaMapper.toResponseDTO(agenda);
    }

    @Transactional
    public AgendaResponseDTO crear(AgendaRequestDTO dto) {
        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario no encontrado"));
        TipoAgenda tipo = tipoAgendaRepository.findById(dto.getTipoId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoAgenda no encontrado"));

        boolean disponible = validarDisponibilidad(dto.getFuncionarioId(), dto.getFecha(), dto.getHoraInicio(), dto.getHoraFin(), null);
        if (!disponible) {
            throw new BusinessException("El funcionario ya tiene un evento en ese horario");
        }

        Agenda agenda = agendaMapper.toEntity(dto);
        agenda.setFuncionario(funcionario);
        agenda.setTipo(tipo);
        agenda.setActivo(true);
        return agendaMapper.toResponseDTO(agendaRepository.save(agenda));
    }

    @Transactional
    public AgendaResponseDTO actualizar(Integer id, AgendaRequestDTO dto) {

        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agenda no encontrada"));

        boolean disponible = validarDisponibilidad(dto.getFuncionarioId(), dto.getFecha(), dto.getHoraInicio(), dto.getHoraFin(), id);
        if (!disponible) {
            throw new BusinessException(
                    "El funcionario ya tiene un evento en ese horario"
            );
        }

        agenda.setFecha(dto.getFecha());
        agenda.setHoraInicio(dto.getHoraInicio());
        agenda.setHoraFin(dto.getHoraFin());
        agenda.setDescripcion(dto.getDescripcion());

        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario no encontrado"));
        TipoAgenda tipo = tipoAgendaRepository.findById(dto.getTipoId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoAgenda no encontrado"));
        agenda.setFuncionario(funcionario);
        agenda.setTipo(tipo);

        return agendaMapper.toResponseDTO(agendaRepository.save(agenda));
    }

    @Transactional
    public void darDeBaja(Integer id) {

        Agenda agenda = agendaRepository.findById(id).orElseThrow(() -> new RuntimeException("Agenda no encontrada con id: " + id));

        if (!agenda.getActivo()) {
            throw new IllegalStateException("La agenda ya esta dada de baja");
        }

        agenda.setActivo(false);
        agenda.setFechaBaja(LocalDateTime.now());

        agendaRepository.save(agenda);
    }

    @Transactional
    public AgendaResponseDTO darDeAlta(Integer id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agenda no encontrada"));

        if (agenda.getActivo()) {
            throw new BusinessException("La agenda ya está activa");
        }

        agenda.setActivo(true);
        agenda.setFechaBaja(null);
        return agendaMapper.toResponseDTO(agendaRepository.save(agenda));
    }

    public List<AgendaResponseDTO> filtrar(AgendaFilterRequestDTO filtro) {
        if (filtro.getFecha() != null && filtro.getFuncionarioId() != null) {
            return agendaRepository
                    .findByFechaAndFuncionarioId(filtro.getFecha(), filtro.getFuncionarioId())
                    .stream().map(agendaMapper::toResponseDTO).toList();
        }
        if (filtro.getFechaDesde() != null && filtro.getFechaHasta() != null) {
            return agendaRepository
                    .findByFechaBetween(filtro.getFechaDesde(), filtro.getFechaHasta())
                    .stream().map(agendaMapper::toResponseDTO).toList();
        }
        if (filtro.getFuncionarioId() != null) {
            return agendaRepository
                    .findByFuncionarioId(filtro.getFuncionarioId())
                    .stream().map(agendaMapper::toResponseDTO).toList();
        }
        if (filtro.getTipoId() != null) {
            return agendaRepository
                    .findByTipoId(filtro.getTipoId())
                    .stream().map(agendaMapper::toResponseDTO).toList();
        }
        if (filtro.getFecha() != null) {
            return agendaRepository
                    .findByFecha(filtro.getFecha())
                    .stream().map(agendaMapper::toResponseDTO).toList();
        }
        return listarActivos();
    }

    public List<AgendaResponseDTO> detectarConflictos(Integer funcionarioId, LocalDate fecha) {
        return agendaRepository
                .findByFechaAndFuncionarioId(fecha, funcionarioId)
                .stream()
                .collect(Collectors.groupingBy(a ->
                        a.getHoraInicio() + "-" + a.getHoraFin()
                ))
                .values().stream()
                .filter(lista -> lista.size() > 1)
                .flatMap(Collection::stream)
                .map(agendaMapper::toResponseDTO)
                .toList();
    }

    public boolean validarDisponibilidad(Integer funcionarioId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, Integer excluirId) {

        List<Agenda> solapadas = agendaRepository.findByFuncionarioIdAndFechaAndHoraInicioLessThanAndHoraFinGreaterThanAndActivoTrue(funcionarioId, fecha, horaFin, horaInicio);

        if (excluirId != null) {
            solapadas = solapadas.stream()
                    .filter(a -> !a.getId().equals(excluirId))
                    .toList();
        }
        return solapadas.isEmpty();
    }

    public List<AgendaResponseDTO> sugerirReprogramacion(
            Integer funcionarioId, LocalDate fecha,
            LocalTime horaInicio, LocalTime horaFin) {

        List<Agenda> ocupadas = agendaRepository
                .findByFuncionarioIdAndFechaAndHoraInicioLessThanAndHoraFinGreaterThanAndActivoTrue(
                        funcionarioId, fecha, horaFin, horaInicio);

        long duracionMinutos = java.time.Duration.between(horaInicio, horaFin).toMinutes();

        LocalTime inicio = LocalTime.of(7, 0);
        LocalTime fin    = LocalTime.of(19, 0);

        List<Agenda> todasDelDia = agendaRepository
                .findByFechaAndFuncionarioId(fecha, funcionarioId)
                .stream()
                .filter(Agenda::getActivo)
                .sorted(Comparator.comparing(Agenda::getHoraInicio))
                .toList();

        List<LocalTime[]> sugerencias = new ArrayList<>();
        LocalTime cursor = inicio;

        for (Agenda a : todasDelDia) {
            if (cursor.plusMinutes(duracionMinutos).compareTo(a.getHoraInicio()) <= 0) {
                sugerencias.add(new LocalTime[]{cursor, cursor.plusMinutes(duracionMinutos)});
            }
            if (a.getHoraFin().isAfter(cursor)) cursor = a.getHoraFin();
        }
        if (cursor.plusMinutes(duracionMinutos).compareTo(fin) <= 0) {
            sugerencias.add(new LocalTime[]{cursor, cursor.plusMinutes(duracionMinutos)});
        }

        return sugerencias.stream().limit(3).map(s -> {
            AgendaResponseDTO dto = new AgendaResponseDTO();
            dto.setFecha(fecha);
            dto.setHoraInicio(s[0]);
            dto.setHoraFin(s[1]);
            dto.setDescripcion("Horario sugerido disponible");
            return dto;
        }).toList();
    }

    public Map<String, Object> detectarSobrecarga(LocalDate fecha, int maxEventosPorDia) {
        List<Agenda> agendas = agendaRepository.findByFecha(fecha)
                .stream().filter(Agenda::getActivo).toList();

        Map<Funcionario, List<Agenda>> porFuncionario = agendas.stream()
                .collect(Collectors.groupingBy(Agenda::getFuncionario));

        List<Map<String, Object>> sobrecargados = porFuncionario.entrySet().stream()
                .filter(e -> e.getValue().size() > maxEventosPorDia)
                .map(e -> {
                    long minutosTotales = e.getValue().stream()
                            .mapToLong(a -> Duration.between(a.getHoraInicio(), a.getHoraFin()).toMinutes())
                            .sum();

                    Map<String, Object> item = new HashMap<>();
                    item.put("funcionarioId",   e.getKey().getId());
                    item.put("nombre",          e.getKey().getNombre() + " " + e.getKey().getApellido());
                    item.put("cantidadEventos", e.getValue().size());
                    item.put("horasOcupadas",   minutosTotales / 60.0);
                    return item;
                })
                .toList();

        return Map.of(
                "fecha", fecha,
                "umbralMaximo", maxEventosPorDia,
                "funcionariosSobrecargados", sobrecargados
        );
    }
}