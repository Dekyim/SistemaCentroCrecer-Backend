package tip.java.sistemacentrocrecer.biz.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.entities.*;
import tip.java.sistemacentrocrecer.biz.dao.repositories.*;
import tip.java.sistemacentrocrecer.dto.DetalleAgendaRequestDTO;
import tip.java.sistemacentrocrecer.dto.DetalleAgendaResponseDTO;
import tip.java.sistemacentrocrecer.mapper.DetalleAgendaMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class DetalleAgendaService {

    private final DetalleAgendaRepository detalleAgendaRepository;
    private final AgendaRepository agendaRepository;
    private final SubtipoAgendaRepository subtipoAgendaRepository;
    private final DetalleAgendaMapper detalleAgendaMapper;

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
    public DetalleAgendaResponseDTO actualizar(Integer id, DetalleAgendaRequestDTO dto) {

        DetalleAgenda detalleAgenda = detalleAgendaRepository.findById(id).orElseThrow(() -> new RuntimeException("Detalle Agenda no encontrado con id: " + id));

        detalleAgenda.setDescripcionEspecifica(dto.getDescripcionEspecifica());
        detalleAgenda.setRequiereParticipantes(dto.getRequiereParticipantes());


        Agenda agenda = agendaRepository.findById(dto.getAgendaId()).orElseThrow(() -> new RuntimeException("Agenda no encontrada con id: " + dto.getAgendaId()));
        SubtipoAgenda subtipo = subtipoAgendaRepository.findById(dto.getSubtipoAgendaId()).orElseThrow(() -> new RuntimeException("SubtipoAgenda no encontrado con id: " + dto.getSubtipoAgendaId()));
        detalleAgenda.setAgenda(agenda);
        detalleAgenda.setSubtipoAgenda(subtipo);

        return detalleAgendaMapper.toResponseDTO(detalleAgendaRepository.save(detalleAgenda));
    }

    @Transactional
    public void darDeBaja(Integer id) {

        DetalleAgenda detalleAgenda = detalleAgendaRepository.findById(id).orElseThrow(() -> new RuntimeException("Detalle Agenda no encontrado con id: " + id));

        if (!detalleAgenda.getActivo()) {
            throw new IllegalStateException("El detalle agenda ya esta dado de baja");
        }

        detalleAgenda.setActivo(false);
        detalleAgenda.setFechaBaja(LocalDateTime.now());

        detalleAgendaRepository.save(detalleAgenda);
    }
}