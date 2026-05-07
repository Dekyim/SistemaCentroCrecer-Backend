package tip.java.sistemacentrocrecer.biz.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.entities.Responsable;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ResponsableRepository;
import tip.java.sistemacentrocrecer.dto.ResponsableRequestDTO;
import tip.java.sistemacentrocrecer.dto.ResponsableResponseDTO;
import tip.java.sistemacentrocrecer.mapper.ResponsableMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponsableService {
    private final ResponsableRepository responsableRepository;
    private final ResponsableMapper responsableMapper;

    public List<ResponsableResponseDTO> listar() {
        return responsableRepository.findAll()
                .stream()
                .map(responsableMapper::toDTO)
                .toList();
    }

    public List<ResponsableResponseDTO> listarActivos() {
        return responsableRepository.findByActivoTrue()
                .stream()
                .map(responsableMapper::toDTO)
                .toList();
    }

    public ResponsableResponseDTO buscarPorId(Integer id) {

        Responsable responsable = responsableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Responsable no encontrado"));

        return responsableMapper.toDTO(responsable);
    }

    public ResponsableResponseDTO crear(ResponsableRequestDTO dto) {

        if (responsableRepository.findByCedula(dto.getCedula()).isPresent()) {
            throw new RuntimeException("Ya existe un responsable con esa cédula");
        }

        if (responsableRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Ya existe un responsable con ese email");
        }

        Responsable responsable = responsableMapper.toEntity(dto);

        responsable.setActivo(true);
        responsable.setFechaBaja(null);

        Responsable guardado = responsableRepository.save(responsable);

        return responsableMapper.toDTO(guardado);
    }

    public ResponsableResponseDTO actualizar(Integer id, ResponsableRequestDTO dto) {

        Responsable responsable = responsableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Responsable no encontrado"));

        responsableMapper.updateEntityFromDTO(dto, responsable);

        Responsable actualizado = responsableRepository.save(responsable);

        return responsableMapper.toDTO(actualizado);
    }

    public void bajaLogica(Integer id) {

        Responsable responsable = responsableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Responsable no encontrado"));

        responsable.setActivo(false);
        responsable.setFechaBaja(LocalDateTime.now());

        responsableRepository.save(responsable);
    }
}
