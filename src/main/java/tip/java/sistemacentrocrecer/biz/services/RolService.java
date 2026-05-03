package tip.java.sistemacentrocrecer.biz.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.entities.Rol;
import tip.java.sistemacentrocrecer.biz.dao.repositories.RolRepository;
import tip.java.sistemacentrocrecer.dto.RolResponseDTO;
import tip.java.sistemacentrocrecer.dto.RolRequestDTO;
import tip.java.sistemacentrocrecer.exceptions.BusinessException;
import tip.java.sistemacentrocrecer.exceptions.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RolService {
    private final RolRepository rolRepository;

    public List<RolRequestDTO> listarTodos() {
        return rolRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<RolRequestDTO> listarActivos() {
        return rolRepository.findByActivoTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public RolRequestDTO actualizar(Integer id, RolResponseDTO dto) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", id));

        if (rolRepository.existsByNombreIgnoreCaseAndIdNot(dto.getNombre(), id)) {
            throw new BusinessException("Ya existe un rol con el nombre: " + dto.getNombre());
        }

        rol.setNombre(dto.getNombre());

        if (dto.getPadreId() != null) {
            if (dto.getPadreId().equals(id)) {
                throw new BusinessException("Un rol no puede ser su propio padre");
            }
            Rol padre = rolRepository.findById(dto.getPadreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Rol padre", dto.getPadreId()));
            rol.setPadre(padre);
        } else {
            rol.setPadre(null);
        }

        return toDTO(rolRepository.save(rol));
    }

    @Transactional
    public void darDeBaja(Integer id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", id));

        if (!rol.isActivo()) {
            throw new BusinessException("El rol ya se encuentra inactivo");
        }

        if (rol.getFuncionario() != null && rol.getFuncionario().isActivo()) {
            throw new BusinessException("No se puede dar de baja el rol porque tiene un funcionario activo asignado");
        }

        rol.setActivo(false);
        rol.setFechaBaja(LocalDate.now());
        rolRepository.save(rol);
    }

    @Transactional
    public RolRequestDTO crear(RolResponseDTO dto) {
        if (rolRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new BusinessException("Ya existe un rol con el nombre: " + dto.getNombre());
        }

        Rol rol = Rol.builder()
                .nombre(dto.getNombre())
                .activo(true)
                .build();

        if (dto.getPadreId() != null) {
            Rol padre = rolRepository.findById(dto.getPadreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Rol padre", dto.getPadreId()));
            rol.setPadre(padre);
        }

        return toDTO(rolRepository.save(rol));
    }


    //Posbiblemente agregar un directorio nuevo "mapper"
    //Convertir un objeto de tipo Rol (entity) a un dto (lo que se muestra en el front)
    private RolRequestDTO toDTO(Rol rol) {
        RolRequestDTO dto = new RolRequestDTO();
        dto.setId(rol.getId());
        dto.setNombre(rol.getNombre());
        dto.setActivo(rol.isActivo());
        dto.setFechaBaja(rol.getFechaBaja());
        if (rol.getPadre() != null) {
            dto.setPadreId(rol.getPadre().getId());
            dto.setPadreNombre(rol.getPadre().getNombre());
        }
        return dto;
    }


}
