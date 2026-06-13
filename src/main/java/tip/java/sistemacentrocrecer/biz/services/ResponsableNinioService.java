package tip.java.sistemacentrocrecer.biz.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;
import tip.java.sistemacentrocrecer.biz.dao.entities.Responsable;
import tip.java.sistemacentrocrecer.biz.dao.entities.ResponsableNinio;
import tip.java.sistemacentrocrecer.biz.dao.repositories.NinioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ResponsableNinioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ResponsableRepository;
import tip.java.sistemacentrocrecer.dto.ResponsableNinioRequestDTO;
import tip.java.sistemacentrocrecer.dto.ResponsableNinioResponseDTO;
import tip.java.sistemacentrocrecer.mapper.ResponsableNinioMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class ResponsableNinioService {
    private final ResponsableNinioRepository responsableNinioRepository;
    private final ResponsableRepository responsableRepository;
    private final NinioRepository ninioRepository;
    private final ResponsableNinioMapper responsableNinioMapper;

    @Transactional(readOnly = true)
    public List<ResponsableNinioResponseDTO> listarPorNinio(Integer ninioId) {
        return responsableNinioRepository.findByNinioId(ninioId)
                .stream()
                .map(responsableNinioMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ResponsableNinioResponseDTO> listarTodos() {
        return responsableNinioRepository.findAll()
                .stream()
                .map(responsableNinioMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ResponsableNinioResponseDTO obtenerPorId(Integer id) {
        ResponsableNinio responsableNinio = responsableNinioRepository.findById(id).orElseThrow(() -> new RuntimeException("ResponsableNinio no encontrado"));
        return responsableNinioMapper.toResponseDTO(responsableNinio);
    }

    @Transactional
    public ResponsableNinioResponseDTO crear(ResponsableNinioRequestDTO dto) {
        if (responsableNinioRepository.existsByNinioIdAndResponsableId(dto.getNinioId(), dto.getResponsableId())) {
            throw new RuntimeException("El responsable ya está vinculado a este niño");
        }
        ResponsableNinio responsableNinio = responsableNinioMapper.toEntity(dto);

        Ninio ninio = ninioRepository.findById(dto.getNinioId()).orElseThrow(() -> new RuntimeException("Niño no encontrado"));

        Responsable responsable = responsableRepository.findById(dto.getResponsableId()).orElseThrow(() -> new RuntimeException("Responsable no encontrado"));

        responsableNinio.setNinio(ninio);
        responsableNinio.setResponsable(responsable);

        responsableNinio = responsableNinioRepository.save(responsableNinio);

        return responsableNinioMapper.toResponseDTO(responsableNinio);
    }

    @Transactional
    public ResponsableNinioResponseDTO actualizar(Integer id, ResponsableNinioRequestDTO dto) {
        ResponsableNinio responsableNinio = responsableNinioRepository.findById(id).orElseThrow(() -> new RuntimeException("ResponsableNinio no encontrado"));

        Ninio ninio = ninioRepository.findById(dto.getNinioId()).orElseThrow(() -> new RuntimeException("Niño no encontrado"));

        Responsable responsable = responsableRepository.findById(dto.getResponsableId()).orElseThrow(() -> new RuntimeException("Responsable no encontrado"));

        responsableNinio.setNinio(ninio);
        responsableNinio.setResponsable(responsable);
        responsableNinio.setTipoRelacion(dto.getTipoRelacion());
        responsableNinio.setAutorizadoRetiro(dto.getAutorizadoRetiro());

        responsableNinio = responsableNinioRepository.save(responsableNinio);

        return responsableNinioMapper.toResponseDTO(responsableNinio);
    }

    @Transactional
    public void eliminar(Integer id) {
        ResponsableNinio responsableNinio =
                responsableNinioRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("ResponsableNinio no encontrado"));
        responsableNinioRepository.delete(responsableNinio);
    }
}