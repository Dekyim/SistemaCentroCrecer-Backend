package tip.java.sistemacentrocrecer.biz.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import tip.java.sistemacentrocrecer.biz.dao.entities.Responsable;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ResponsableRepository;
import tip.java.sistemacentrocrecer.dto.CambiarContraseniaRequestDTO;
import tip.java.sistemacentrocrecer.dto.CambiarContraseniaResponseDTO;
import tip.java.sistemacentrocrecer.dto.ResponsableRequestDTO;
import tip.java.sistemacentrocrecer.dto.ResponsableResponseDTO;
import tip.java.sistemacentrocrecer.exceptions.BusinessException;
import tip.java.sistemacentrocrecer.exceptions.ResourceNotFoundException;
import tip.java.sistemacentrocrecer.mapper.ResponsableMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponsableService {
    private final ResponsableRepository responsableRepository;
    private final ResponsableMapper responsableMapper;
    private final PasswordEncoder passwordEncoder;

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

    @Transactional
    public ResponsableResponseDTO crear(ResponsableRequestDTO dto) {

        if (responsableRepository.findByCedula(dto.getCedula()).isPresent()) {
            throw new RuntimeException("Ya existe un responsable con esa cédula");
        }

        if (responsableRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Ya existe un responsable con ese email");
        }

        Responsable responsable = responsableMapper.toEntity(dto);
        responsable.setContrasenia(passwordEncoder.encode(dto.getContrasenia()));

        responsable.setActivo(true);
        responsable.setFechaBaja(null);

        Responsable guardado = responsableRepository.save(responsable);

        return responsableMapper.toDTO(guardado);
    }

    @Transactional
    public ResponsableResponseDTO actualizar(Integer id, ResponsableRequestDTO dto) {

        Responsable responsable = responsableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Responsable no encontrado"));
        if (dto.getContrasenia() != null && !dto.getContrasenia().isBlank()) {
            responsable.setContrasenia(passwordEncoder.encode(dto.getContrasenia()));
        }

        responsableMapper.updateEntityFromDTO(dto, responsable);

        Responsable actualizado = responsableRepository.save(responsable);

        return responsableMapper.toDTO(actualizado);
    }

    @Transactional
    public void bajaLogica(Integer id) {

        Responsable responsable = responsableRepository.findById(id).orElseThrow(() -> new RuntimeException("Responsable no encontrado"));

        responsable.setActivo(false);
        responsable.setFechaBaja(LocalDateTime.now());

        responsableRepository.save(responsable);
    }

    @Transactional
    public CambiarContraseniaResponseDTO cambiarPassword(Integer id, CambiarContraseniaRequestDTO requestDTO) {

        String nuevaContrasenia = requestDTO.getNuevaContrasenia();

        if (nuevaContrasenia == null || nuevaContrasenia.length() < 8) {
            throw new BusinessException("La contraseña debe tener al menos 8 caracteres");
        }

        Responsable responsable = responsableRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Responsable", id));

        responsable.setContrasenia(passwordEncoder.encode(nuevaContrasenia));

        responsableRepository.save(responsable);

        return new CambiarContraseniaResponseDTO("Contraseña actualizada exitosamente");
    }
}
