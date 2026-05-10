package tip.java.sistemacentrocrecer.biz.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tip.java.sistemacentrocrecer.biz.dao.entities.Inscripcion;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;
import tip.java.sistemacentrocrecer.biz.dao.entities.Responsable;
import tip.java.sistemacentrocrecer.biz.dao.repositories.InscripcionRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.NinioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ResponsableRepository;
import tip.java.sistemacentrocrecer.dto.InscripcionRequestDTO;
import tip.java.sistemacentrocrecer.dto.InscripcionResponseDTO;
import tip.java.sistemacentrocrecer.mapper.InscripcionMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InscripcionService {
    private final InscripcionRepository inscripcionRepository;
    private final NinioRepository ninioRepository;
    private final ResponsableRepository responsableRepository;
    private final InscripcionMapper inscripcionMapper;

    @Transactional(readOnly = true)
    public List<InscripcionResponseDTO> listarTodos() {

        return inscripcionRepository.findAll()
                .stream()
                .map(inscripcionMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public InscripcionResponseDTO obtenerPorId(Integer id) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));

        return inscripcionMapper.toResponseDTO(inscripcion);
    }

    @Transactional
    public InscripcionResponseDTO crear(InscripcionRequestDTO dto) {
        Inscripcion inscripcion = inscripcionMapper.toEntity(dto);

        Ninio ninio = ninioRepository.findById(dto.getNinioId())
                .orElseThrow(() -> new RuntimeException("Niño no encontrado"));

        Responsable responsable = responsableRepository.findById(dto.getResponsableId())
                .orElseThrow(() -> new RuntimeException("Responsable no encontrado"));

        inscripcion.setNinio(ninio);
        inscripcion.setResponsables(List.of(responsable));

        inscripcion = inscripcionRepository.save(inscripcion);

        return inscripcionMapper.toResponseDTO(inscripcion);
    }

    @Transactional
    public InscripcionResponseDTO actualizar(Integer id, InscripcionRequestDTO dto) {

        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));

        Ninio ninio = ninioRepository.findById(dto.getNinioId())
                .orElseThrow(() -> new RuntimeException("Niño no encontrado"));

        Responsable responsable = responsableRepository.findById(dto.getResponsableId())
                .orElseThrow(() -> new RuntimeException("Responsable no encontrado"));

        inscripcion.setNinio(ninio);
        inscripcion.setResponsables(List.of(responsable));
        inscripcion.setFechaInscripcion(dto.getFechaInscripcion());
        inscripcion.setFechaInicio(dto.getFechaInicio());
        inscripcion.setFechaFin(dto.getFechaFin());
        inscripcion.setEstadoInscripcion(dto.getEstadoInscripcion());
        inscripcion.setObservaciones(dto.getObservaciones());

        inscripcion = inscripcionRepository.save(inscripcion);

        return inscripcionMapper.toResponseDTO(inscripcion);
    }

    @Transactional
    public void eliminar(Integer id) {
        Inscripcion inscripcion = inscripcionRepository.findById(id).orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));
        inscripcionRepository.delete(inscripcion);
    }
}
