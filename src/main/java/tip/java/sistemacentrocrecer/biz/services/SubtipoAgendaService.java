package tip.java.sistemacentrocrecer.biz.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.entities.*;
import tip.java.sistemacentrocrecer.biz.dao.repositories.*;
import tip.java.sistemacentrocrecer.dto.SubtipoAgendaRequestDTO;
import tip.java.sistemacentrocrecer.dto.SubtipoAgendaResponseDTO;
import tip.java.sistemacentrocrecer.mapper.SubtipoAgendaMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class SubtipoAgendaService {
    private final SubtipoAgendaRepository subtipoAgendaRepository;
    private final DetalleAgendaRepository detalleAgendaRepository;
    private final AgendaLimpiezaRepository agendaLimpiezaRepository;
    private final SubtipoAgendaMapper subtipoAgendaMapper;


    public List<SubtipoAgendaResponseDTO> listarTodos() {
        return subtipoAgendaRepository.findAll()
                .stream()
                .map(subtipoAgendaMapper::toResponseDTO)
                .toList();
    }

    public SubtipoAgendaResponseDTO obtenerPorId(Integer id) {
        SubtipoAgenda subtipoAgenda = subtipoAgendaRepository.findById(id).orElseThrow(() -> new RuntimeException("Subtipo Agenda no encontrado con id: " + id));
        return subtipoAgendaMapper.toResponseDTO(subtipoAgenda);
    }

    @Transactional
    public SubtipoAgendaResponseDTO crear(SubtipoAgendaRequestDTO dto) {

        SubtipoAgenda subtipoAgenda = subtipoAgendaMapper.toEntity(dto);

        DetalleAgenda detalleAgenda = detalleAgendaRepository.findById(dto.getDetalleAgendaId()).orElseThrow(() -> new RuntimeException("Detalle Agenda no encontrado con id: " + dto.getDetalleAgendaId()));
        AgendaLimpieza agendaLimpieza = agendaLimpiezaRepository.findById(dto.getAgendaLimpiezaId()).orElseThrow(() -> new RuntimeException("Agenda Limpieza no encontrada con id: " + dto.getAgendaLimpiezaId()));

        subtipoAgenda.setDetalleAgenda(detalleAgenda);
        subtipoAgenda.setAgendaLimpieza(agendaLimpieza);

        return subtipoAgendaMapper.toResponseDTO(subtipoAgendaRepository.save(subtipoAgenda));
    }

    @Transactional
    public SubtipoAgendaResponseDTO actualizar(Integer id, SubtipoAgendaRequestDTO dto) {

        SubtipoAgenda subtipoAgenda = subtipoAgendaRepository.findById(id).orElseThrow(() -> new RuntimeException("Subtipo Agenda no encontrado con id: " + id));

        subtipoAgenda.setSubtipo(dto.getSubtipo());

        DetalleAgenda detalleAgenda = detalleAgendaRepository.findById(dto.getDetalleAgendaId()).orElseThrow(() -> new RuntimeException("Detalle Agenda no encontrado con id: " + dto.getDetalleAgendaId()));
        AgendaLimpieza agendaLimpieza = agendaLimpiezaRepository.findById(dto.getAgendaLimpiezaId()).orElseThrow(() -> new RuntimeException("Agenda Limpieza no encontrada con id: " + dto.getAgendaLimpiezaId()));

        subtipoAgenda.setDetalleAgenda(detalleAgenda);
        subtipoAgenda.setAgendaLimpieza(agendaLimpieza);

        return subtipoAgendaMapper.toResponseDTO(subtipoAgendaRepository.save(subtipoAgenda));
    }

}