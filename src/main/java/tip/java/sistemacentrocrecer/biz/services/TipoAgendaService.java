package tip.java.sistemacentrocrecer.biz.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.entities.*;
import tip.java.sistemacentrocrecer.biz.dao.repositories.*;
import tip.java.sistemacentrocrecer.dto.TipoAgendaRequestDTO;
import tip.java.sistemacentrocrecer.dto.TipoAgendaResponseDTO;
import tip.java.sistemacentrocrecer.mapper.TipoAgendaMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class TipoAgendaService {
    private final TipoAgendaRepository tipoAgendaRepository;
    private final TipoAgendaMapper tipoAgendaMapper;


    public List<TipoAgendaResponseDTO> listarTodos() {
        return tipoAgendaRepository.findAll()
                .stream()
                .map(tipoAgendaMapper::toResponseDTO)
                .toList();
    }

    public TipoAgendaResponseDTO obtenerPorId(Integer id) {
        TipoAgenda tipoAgenda = tipoAgendaRepository.findById(id).orElseThrow(() -> new RuntimeException("Tipo Agenda no encontrado con id: " + id));
        return tipoAgendaMapper.toResponseDTO(tipoAgenda);
    }

    @Transactional
    public TipoAgendaResponseDTO crear(TipoAgendaRequestDTO dto) {

        TipoAgenda tipoAgenda = tipoAgendaMapper.toEntity(dto);

        return tipoAgendaMapper.toResponseDTO(tipoAgendaRepository.save(tipoAgenda));
    }

    @Transactional
    public TipoAgendaResponseDTO actualizar(Integer id, TipoAgendaRequestDTO dto) {

        TipoAgenda tipoAgenda = tipoAgendaRepository.findById(id).orElseThrow(() -> new RuntimeException("Tipo Agenda no encontrado con id: " + id));

        tipoAgenda.setTipo(dto.getTipo());

        return tipoAgendaMapper.toResponseDTO(tipoAgendaRepository.save(tipoAgenda));
    }

}