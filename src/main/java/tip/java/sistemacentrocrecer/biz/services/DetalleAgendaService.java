package tip.java.sistemacentrocrecer.biz.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.entities.*;
import tip.java.sistemacentrocrecer.biz.dao.repositories.*;
import tip.java.sistemacentrocrecer.dto.AgendaRequestDTO;
import tip.java.sistemacentrocrecer.dto.AgendaResponseDTO;
import tip.java.sistemacentrocrecer.dto.DetalleAgendaRequestDTO;
import tip.java.sistemacentrocrecer.dto.DetalleAgendaResponseDTO;
import tip.java.sistemacentrocrecer.mapper.AgendaMapper;
import tip.java.sistemacentrocrecer.mapper.DetalleAgendaMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class DetalleAgendaService {

    private final DetalleAgendaRepository detalleAgendaRepository;
    private final AgendaRepository agendaRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final SubtipoAgendaRepository subtipoAgendaRepository;
    private final DetalleAgendaMapper detalleAgendaMapper;
    private final AgendaMapper agendaMapper;
    private final TipoAgendaRepository tipoAgendaRepository;

    public List<DetalleAgendaResponseDTO> listarTodos() {
        return detalleAgendaRepository.findAll()
                .stream()
                .map(detalleAgendaMapper::toResponseDTO)
                .toList();
    }

    public List<DetalleAgendaResponseDTO> listarActivos() {
        return detalleAgendaRepository.findByActivoTrue()
                .stream()
                .map(detalleAgendaMapper::toResponseDTO)
                .toList();
    }

    public DetalleAgendaResponseDTO obtenerPorId(Integer id) {
        DetalleAgenda detalleAgenda = detalleAgendaRepository.findById(id).orElseThrow(() -> new RuntimeException("Detalle de agenda no encontrado con id: " + id));
        return detalleAgendaMapper.toResponseDTO(detalleAgenda);
    }

    @Transactional
    public DetalleAgendaResponseDTO crear(DetalleAgendaRequestDTO dto) {

        DetalleAgenda detalleAgenda = detalleAgendaMapper.toEntity(dto);

        Agenda agenda = agendaRepository.findById(dto.getAgendaId()).orElseThrow(() -> new RuntimeException("Agenda no encontrada con id: " + dto.getAgendaId()));
        SubtipoAgenda subtipo = subtipoAgendaRepository.findById(dto.getSubtipoAgendaId()).orElseThrow(() -> new RuntimeException("SubtipoAgenda no encontrado con id: " + dto.getSubtipoAgendaId()));

        detalleAgenda.setAgenda(agenda);
        detalleAgenda.setSubtipoAgenda(subtipo);

        return detalleAgendaMapper.toResponseDTO(detalleAgendaRepository.save(detalleAgenda));
    }

    @Transactional
    public AgendaResponseDTO actualizar(Integer id, AgendaRequestDTO dto) {

        Agenda agenda = agendaRepository.findById(id).orElse(null);

        agenda.setFecha(dto.getFecha());
        agenda.setHoraInicio(dto.getHoraInicio());
        agenda.setHoraFin(dto.getHoraFin());
        agenda.setDescripcion(dto.getDescripcion());

        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId()).orElseThrow(() -> new RuntimeException("Funcionario no encontrado con id: " + dto.getFuncionarioId()));
        TipoAgenda tipo = tipoAgendaRepository.findById(dto.getTipoId())
                .orElseThrow(() ->
                        new RuntimeException("TipoAgenda no encontrado con id: " + dto.getTipoId()));        agenda.setFuncionario(funcionario);
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
}