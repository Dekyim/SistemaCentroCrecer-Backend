package tip.java.sistemacentrocrecer.biz.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tip.java.sistemacentrocrecer.biz.dao.entities.Funcionario;
import tip.java.sistemacentrocrecer.biz.dao.entities.Rol;
import tip.java.sistemacentrocrecer.biz.dao.repositories.FuncionarioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.RolRepository;
import tip.java.sistemacentrocrecer.dto.FuncionarioResponseDTO;
import tip.java.sistemacentrocrecer.dto.FuncionarioRequestDTO;
import tip.java.sistemacentrocrecer.exceptions.BusinessException;
import tip.java.sistemacentrocrecer.exceptions.ResourceNotFoundException;
import tip.java.sistemacentrocrecer.mapper.FuncionarioMapper;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor

public class FuncionarioService {
    private final FuncionarioRepository funcionarioRepository;
    private final RolRepository rolRepository;
    private final FuncionarioMapper funcionarioMapper;

    @Transactional
    public FuncionarioRequestDTO crear(FuncionarioResponseDTO dto) {

        if (funcionarioRepository.existsByCedula(dto.getCedula())) {
            throw new BusinessException("Ya existe un funcionario con la cédula");
        }

        if (funcionarioRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Ya existe un funcionario con el email");
        }

        Rol rol = rolRepository.findById(dto.getRol().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol", dto.getRol().getId()));

        if (!rol.isActivo()) {
            throw new BusinessException("No se puede asignar un rol inactivo");
        }

        Funcionario funcionario = funcionarioMapper.toEntity(dto);
        funcionario.setRol(rol);

        return funcionarioMapper.toResponseDTO(funcionarioRepository.save(funcionario));
    }

    public List<FuncionarioRequestDTO> listarTodos() {
        return funcionarioRepository.findAll().stream()
                .map(funcionarioMapper::toResponseDTO)
                .toList();
    }
    public List<FuncionarioRequestDTO> listarActivos() {
        return funcionarioRepository.findByActivoTrue().stream()
                .map(funcionarioMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public FuncionarioRequestDTO actualizar(Integer id, FuncionarioResponseDTO dto) {

        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario", id));

        if (funcionarioRepository.existsByCedulaAndIdNot(dto.getCedula(), id)) {
            throw new BusinessException("Cédula duplicada");
        }

        if (funcionarioRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new BusinessException("Email duplicado");
        }

        Rol rol = rolRepository.findById(dto.getRol().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol", dto.getRol().getId()));

        funcionario.setCedula(dto.getCedula());
        funcionario.setNombre(dto.getNombre());
        funcionario.setApellido(dto.getApellido());
        funcionario.setEmail(dto.getEmail());
        funcionario.setTelefono(dto.getTelefono());
        funcionario.setFechaNacimiento(dto.getFechaNacimiento());
        funcionario.setRol(rol);

        if (dto.getContrasenia() != null && !dto.getContrasenia().isBlank()) {
            funcionario.setContrasenia(dto.getContrasenia());
        }

        return funcionarioMapper.toResponseDTO(funcionarioRepository.save(funcionario));
    }

    @Transactional
    public void darDeBaja(Integer id) {
        Funcionario f = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario", id));

        if (!f.isActivo()) {
            throw new BusinessException("Ya está inactivo");
        }

        f.setActivo(false);
        f.setFechaBaja(LocalDate.now());
        funcionarioRepository.save(f);
    }

}
