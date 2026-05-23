package tip.java.sistemacentrocrecer.biz.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.entities.*;
import tip.java.sistemacentrocrecer.biz.dao.enums.EstadoLimpiezaEnum;
import tip.java.sistemacentrocrecer.biz.dao.repositories.*;
import tip.java.sistemacentrocrecer.dto.AgendaLimpiezaRequestDTO;
import tip.java.sistemacentrocrecer.dto.AgendaLimpiezaResponseDTO;
import tip.java.sistemacentrocrecer.exceptions.ResourceNotFoundException;
import tip.java.sistemacentrocrecer.mapper.AgendaLimpiezaMapper;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class AgendaLimpiezaService {

    private final AgendaLimpiezaRepository agendaLimpiezaRepository;
    private final AgendaRepository agendaRepository;
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
        AgendaLimpieza agendaLimpieza = agendaLimpiezaRepository.findById(id).orElseThrow(() -> new RuntimeException("AgendaLimpieza no encontrada con id: " + id));
        return agendaLimpiezaMapper.toResponseDTO(agendaLimpieza);
    }

    @Transactional
    public AgendaLimpiezaResponseDTO crear(AgendaLimpiezaRequestDTO dto) {

        if (agendaLimpiezaRepository.existsByAgendaIdAndSubtipoAgendaSubtipoId(
                dto.getAgendaId(), dto.getSubtipoAgendaId())) {
            throw new IllegalStateException("Ya existe una limpieza para esa agenda y subtipo");
        }

        Agenda agenda = agendaRepository.findById(dto.getAgendaId())
                .orElseThrow(() -> new RuntimeException("Agenda no encontrada"));
        SubtipoAgenda subtipoAgenda = subtipoAgendaRepository.findById(dto.getSubtipoAgendaId())
                .orElseThrow(() -> new RuntimeException("SubtipoAgenda no encontrada"));

        AgendaLimpieza agendaLimpieza = agendaLimpiezaMapper.toEntity(dto);
        agendaLimpieza.setAgenda(agenda);
        agendaLimpieza.setSubtipoAgenda(subtipoAgenda);

        return agendaLimpiezaMapper.toResponseDTO(agendaLimpiezaRepository.save(agendaLimpieza));
    }


    @Transactional
    public AgendaLimpiezaResponseDTO actualizar(Integer id, AgendaLimpiezaRequestDTO dto) {

        AgendaLimpieza agendaLimpieza = agendaLimpiezaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AgendaLimpieza no encontrada con id: " + id));

        if (agendaLimpiezaRepository.existsByAgendaIdAndSubtipoAgendaSubtipoIdAndIdNot(
                dto.getAgendaId(), dto.getSubtipoAgendaId(), id)) {
            throw new IllegalStateException("Ya existe una limpieza para esa agenda y subtipo");
        }

        Agenda agenda = agendaRepository.findById(dto.getAgendaId())
                .orElseThrow(() -> new RuntimeException("Agenda no encontrada"));
        SubtipoAgenda subtipoAgenda = subtipoAgendaRepository.findById(dto.getSubtipoAgendaId())
                .orElseThrow(() -> new RuntimeException("SubtipoAgenda no encontrada"));

        agendaLimpieza.setDescripcion(dto.getDescripcion());
        agendaLimpieza.setZona(dto.getZona());
        agendaLimpieza.setFrecuencia(dto.getFrecuencia());
        agendaLimpieza.setAgenda(agenda);
        agendaLimpieza.setSubtipoAgenda(subtipoAgenda);

        return agendaLimpiezaMapper.toResponseDTO(agendaLimpiezaRepository.save(agendaLimpieza));
    }


    @Transactional
    public AgendaLimpiezaResponseDTO cambiarEstado(Integer id, EstadoLimpiezaEnum estado) {

        AgendaLimpieza agendaLimpieza = agendaLimpiezaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AgendaLimpieza no encontrada con id: " + id));

        agendaLimpieza.setEstado(estado);

        return agendaLimpiezaMapper.toResponseDTO(agendaLimpiezaRepository.save(agendaLimpieza));
    }

    @Transactional
    public List<AgendaLimpiezaResponseDTO> listarPorFuncionario(Integer funcionarioId) {
        if (!funcionarioRepository.existsById(funcionarioId)) {
            throw new ResourceNotFoundException("Funcionario no encontrado");
        }
        return agendaLimpiezaRepository.findByAgendaFuncionarioId(funcionarioId)
                .stream()
                .map(agendaLimpiezaMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public List<AgendaLimpiezaResponseDTO> detectarIncumplimientos() {
        LocalDate hoy = LocalDate.now();
        return agendaLimpiezaRepository.findAll()
                .stream()
                .filter(l -> l.getEstado() == EstadoLimpiezaEnum.PENDIENTE
                        || l.getEstado() == EstadoLimpiezaEnum.EN_PROCESO)
                .filter(l -> l.getAgenda() != null
                        && l.getAgenda().getFecha() != null
                        && l.getAgenda().getFecha().isBefore(hoy))
                .map(agendaLimpiezaMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public void eliminar(Integer id) {
        AgendaLimpieza agendaLimpieza = agendaLimpiezaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AgendaLimpieza no encontrada con id: " + id));
        agendaLimpiezaRepository.delete(agendaLimpieza);
    }

}