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
import tip.java.sistemacentrocrecer.dto.AgendaRequestDTO;
import tip.java.sistemacentrocrecer.dto.AgendaResponseDTO;
import tip.java.sistemacentrocrecer.mapper.AgendaMapper;

import java.time.LocalDateTime;
import java.util.List;

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

        Agenda agenda = agendaMapper.toEntity(dto);

        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId()).orElseThrow(() -> new RuntimeException("Funcionario no encontrado con id: " + dto.getFuncionarioId()));
        TipoAgenda tipo = tipoAgendaRepository.findById(dto.getTipoId()).orElseThrow(() -> new RuntimeException("TipoAgenda no encontrado con id: " + dto.getTipoId()));

        agenda.setFuncionario(funcionario);
        agenda.setTipo(tipo);

        return agendaMapper.toResponseDTO(agendaRepository.save(agenda));
    }

    @Transactional
    public AgendaResponseDTO actualizar(Integer id, AgendaRequestDTO dto) {

        Agenda agenda = agendaRepository.findById(id).orElse(null);

        agenda.setFecha(dto.getFecha());
        agenda.setHoraInicio(dto.getHoraInicio());
        agenda.setHoraFin(dto.getHoraFin());
        agenda.setDescripcion(dto.getDescripcion());

        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId()).orElseThrow(() -> new RuntimeException("Funcionario no encontrado con id: " + dto.getFuncionarioId()));
        TipoAgenda tipo = tipoAgendaRepository.findById(dto.getTipoId()).orElseThrow(() -> new RuntimeException("TipoAgenda no encontrado con id: " + dto.getTipoId()));
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
}