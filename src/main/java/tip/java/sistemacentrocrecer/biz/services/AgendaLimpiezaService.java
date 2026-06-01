package tip.java.sistemacentrocrecer.biz.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.entities.AgendaLimpieza;
import tip.java.sistemacentrocrecer.biz.dao.entities.Funcionario;
import tip.java.sistemacentrocrecer.biz.dao.entities.SubtipoAgenda;
import tip.java.sistemacentrocrecer.biz.dao.enums.EstadoLimpiezaEnum;
import tip.java.sistemacentrocrecer.biz.dao.repositories.AgendaLimpiezaRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.FuncionarioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.SubtipoAgendaRepository;
import tip.java.sistemacentrocrecer.dto.AgendaLimpiezaRequestDTO;
import tip.java.sistemacentrocrecer.dto.AgendaLimpiezaResponseDTO;
import tip.java.sistemacentrocrecer.exceptions.ResourceNotFoundException;
import tip.java.sistemacentrocrecer.mapper.AgendaLimpiezaMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AgendaLimpiezaService {

    private final AgendaLimpiezaRepository agendaLimpiezaRepository;
    private final SubtipoAgendaRepository subtipoAgendaRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final AgendaLimpiezaMapper agendaLimpiezaMapper;

    public List<AgendaLimpiezaResponseDTO> listarTodos() {
        return agendaLimpiezaRepository.findAll()
                .stream()
                .map(agendaLimpiezaMapper::toResponseDTO)
                .toList();
    }

    public List<AgendaLimpiezaResponseDTO> listarPorEstado(EstadoLimpiezaEnum estado) {
        return agendaLimpiezaRepository.findByEstado(estado)
                .stream()
                .map(agendaLimpiezaMapper::toResponseDTO)
                .toList();
    }

    public AgendaLimpiezaResponseDTO obtenerPorId(Integer id) {
        AgendaLimpieza agendaLimpieza = agendaLimpiezaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AgendaLimpieza no encontrada con id: " + id));
        return agendaLimpiezaMapper.toResponseDTO(agendaLimpieza);
    }

    @Transactional
    public AgendaLimpiezaResponseDTO crear(AgendaLimpiezaRequestDTO dto) {
        validarSolapamiento(dto.getFuncionarioId(), dto.getFecha(),
                dto.getHoraInicio(), dto.getHoraFin(), null);

        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario no encontrado"));
        SubtipoAgenda subtipoAgenda = subtipoAgendaRepository.findById(dto.getSubtipoAgendaId())
                .orElseThrow(() -> new ResourceNotFoundException("SubtipoAgenda no encontrada"));

        AgendaLimpieza agendaLimpieza = agendaLimpiezaMapper.toEntity(dto);
        agendaLimpieza.setFuncionario(funcionario);
        agendaLimpieza.setSubtipoAgenda(subtipoAgenda);

        return agendaLimpiezaMapper.toResponseDTO(agendaLimpiezaRepository.save(agendaLimpieza));
    }

    @Transactional
    public AgendaLimpiezaResponseDTO actualizar(Integer id, AgendaLimpiezaRequestDTO dto) {
        AgendaLimpieza agendaLimpieza = agendaLimpiezaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AgendaLimpieza no encontrada con id: " + id));

        validarSolapamiento(dto.getFuncionarioId(), dto.getFecha(),
                dto.getHoraInicio(), dto.getHoraFin(), id);

        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario no encontrado"));
        SubtipoAgenda subtipoAgenda = subtipoAgendaRepository.findById(dto.getSubtipoAgendaId())
                .orElseThrow(() -> new ResourceNotFoundException("SubtipoAgenda no encontrada"));

        agendaLimpieza.setDescripcion(dto.getDescripcion());
        agendaLimpieza.setZona(dto.getZona());
        agendaLimpieza.setFrecuencia(dto.getFrecuencia());
        agendaLimpieza.setFecha(dto.getFecha());
        agendaLimpieza.setHoraInicio(dto.getHoraInicio());
        agendaLimpieza.setHoraFin(dto.getHoraFin());
        agendaLimpieza.setFuncionario(funcionario);
        agendaLimpieza.setSubtipoAgenda(subtipoAgenda);

        return agendaLimpiezaMapper.toResponseDTO(agendaLimpiezaRepository.save(agendaLimpieza));
    }

    @Transactional
    public AgendaLimpiezaResponseDTO cambiarEstado(Integer id, EstadoLimpiezaEnum estado) {
        AgendaLimpieza agendaLimpieza = agendaLimpiezaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AgendaLimpieza no encontrada con id: " + id));
        agendaLimpieza.setEstado(estado);
        return agendaLimpiezaMapper.toResponseDTO(agendaLimpiezaRepository.save(agendaLimpieza));
    }

    public List<AgendaLimpiezaResponseDTO> listarPorFuncionario(Integer funcionarioId) {
        if (!funcionarioRepository.existsById(funcionarioId)) {
            throw new ResourceNotFoundException("Funcionario no encontrado");
        }
        return agendaLimpiezaRepository.findByFuncionarioId(funcionarioId)
                .stream()
                .map(agendaLimpiezaMapper::toResponseDTO)
                .toList();
    }

    public List<AgendaLimpiezaResponseDTO> detectarIncumplimientos() {
        LocalDate hoy = LocalDate.now();
        return agendaLimpiezaRepository.findAll()
                .stream()
                .filter(l -> l.getEstado() == EstadoLimpiezaEnum.PENDIENTE
                        || l.getEstado() == EstadoLimpiezaEnum.EN_PROCESO)
                .filter(l -> l.getFecha() != null && l.getFecha().isBefore(hoy))
                .map(agendaLimpiezaMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public void eliminar(Integer id) {
        AgendaLimpieza agendaLimpieza = agendaLimpiezaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AgendaLimpieza no encontrada con id: " + id));
        agendaLimpiezaRepository.delete(agendaLimpieza);
    }

    private void validarSolapamiento(Integer funcionarioId, LocalDate fecha,
                                     LocalTime horaInicio, LocalTime horaFin,
                                     Integer excludeId) {
        String inicioStr = horaInicio != null ? horaInicio.toString() : null;
        String finStr    = horaFin    != null ? horaFin.toString()    : null;

        boolean solapa = excludeId == null
                ? agendaLimpiezaRepository.existsSolapamiento(funcionarioId, fecha, inicioStr, finStr)
                : agendaLimpiezaRepository.existsSolapamientoExcluyendo(funcionarioId, fecha, inicioStr, finStr, excludeId);

        if (solapa) {
            throw new IllegalStateException(
                "El funcionario ya tiene una tarea de limpieza que se superpone con el horario indicado");
        }
    }
}
