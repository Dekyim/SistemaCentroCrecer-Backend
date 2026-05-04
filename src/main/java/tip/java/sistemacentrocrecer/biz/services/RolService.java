package tip.java.sistemacentrocrecer.biz.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.entities.Rol;
import tip.java.sistemacentrocrecer.biz.dao.repositories.RolRepository;
import tip.java.sistemacentrocrecer.dto.RolRequestDTO;
import tip.java.sistemacentrocrecer.dto.RolResponseDTO;
import tip.java.sistemacentrocrecer.exceptions.BusinessException;
import tip.java.sistemacentrocrecer.exceptions.ResourceNotFoundException;
import tip.java.sistemacentrocrecer.mapper.RolMapper;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class RolService {
    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    public List<RolResponseDTO> listarTodos() {
        return rolRepository.findAll()
                .stream()
                .map(rolMapper::toResponseDTO)
                .toList();
    }
    public List<RolResponseDTO> listarActivos() {
        return rolRepository.findByActivoTrue()
                .stream()
                .map(rolMapper::toResponseDTO)
                .toList();
    }
    @Transactional
    public RolResponseDTO crear(RolRequestDTO dto) {

        if (rolRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new BusinessException("Ya existe un rol con ese nombre");
        }

        Rol rol = rolMapper.toEntity(dto);

        if (dto.getPadreId() != null) {
            Rol padre = rolRepository.findById(dto.getPadreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Rol padre", dto.getPadreId()));
            rol.setPadre(padre);
        }

        return rolMapper.toResponseDTO(rolRepository.save(rol));
    }

    @Transactional
    public RolResponseDTO actualizar(Integer id, RolRequestDTO dto) {

        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", id));

        if (rolRepository.existsByNombreIgnoreCaseAndIdNot(dto.getNombre(), id)) {
            throw new BusinessException("Nombre duplicado");
        }

        rol.setNombre(dto.getNombre());

        if (dto.getPadreId() != null) {
            if (dto.getPadreId().equals(id)) {
                throw new BusinessException("No puede ser su propio padre");
            }

            Rol padre = rolRepository.findById(dto.getPadreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Rol padre", dto.getPadreId()));

            rol.setPadre(padre);
        } else {
            rol.setPadre(null);
        }

        return rolMapper.toResponseDTO(rolRepository.save(rol));
    }

    @Transactional
    public void darDeBaja(Integer id) {

        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", id));

        if (!rol.isActivo()) {
            throw new BusinessException("Ya está inactivo");
        }

        rol.setActivo(false);
        rol.setFechaBaja(LocalDate.now());
        rolRepository.save(rol);
    }


}
