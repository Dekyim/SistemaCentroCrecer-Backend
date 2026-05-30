package tip.java.sistemacentrocrecer.biz.services;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tip.java.sistemacentrocrecer.biz.dao.entities.Funcionario;
import tip.java.sistemacentrocrecer.biz.dao.entities.Rol;
import tip.java.sistemacentrocrecer.biz.dao.repositories.FuncionarioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.RolRepository;
import tip.java.sistemacentrocrecer.dto.*;
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
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public FuncionarioResponseDTO crear(FuncionarioRequestDTO dto) {

        if (funcionarioRepository.existsByCedula(dto.getCedula())) {
            throw new BusinessException("Ya existe un funcionario con la cédula");
        }

        if (funcionarioRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Ya existe un funcionario con el email");
        }

        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol", dto.getRolId()));

        if (!rol.isActivo()) {
            throw new BusinessException("No se puede asignar un rol inactivo");
        }

        Funcionario funcionario = funcionarioMapper.toEntity(dto);
        funcionario.setRol(rol);
        funcionario.setContrasenia(passwordEncoder.encode(dto.getContrasenia()));

        return funcionarioMapper.toResponseDTO(funcionarioRepository.save(funcionario));
    }

    @Transactional(readOnly = true)
    public List<FuncionarioResponseDTO> listarTodos() {
        return funcionarioRepository.findAll().stream()
                .map(funcionarioMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FuncionarioResponseDTO> listarActivos() {
        return funcionarioRepository.findByActivoTrue().stream()
                .map(funcionarioMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public FuncionarioResponseDTO actualizar(Integer id, FuncionarioUpdateDTO dto) {

        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario", id));

        if (funcionarioRepository.existsByCedulaAndIdNot(dto.getCedula(), id)) {
            throw new BusinessException("Cédula duplicada");
        }

        if (funcionarioRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new BusinessException("Email duplicado");
        }

        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol", dto.getRolId()));

        // Protección: el rol ADMINISTRADOR_SISTEMA no se puede cambiar ni asignar desde edición
        boolean esAdminActual = funcionario.getRol() != null &&
                "ADMINISTRADOR_SISTEMA".equalsIgnoreCase(funcionario.getRol().getNombre());
        boolean esAdminNuevo = "ADMINISTRADOR_SISTEMA".equalsIgnoreCase(rol.getNombre());

        if (esAdminActual && !esAdminNuevo) {
            throw new BusinessException("No se puede cambiar el rol de un Administrador de Sistema");
        }
        if (!esAdminActual && esAdminNuevo) {
            throw new BusinessException("No se puede asignar el rol de Administrador de Sistema");
        }

        funcionario.setCedula(dto.getCedula());
        funcionario.setNombre(dto.getNombre());
        funcionario.setApellido(dto.getApellido());
        funcionario.setEmail(dto.getEmail());
        funcionario.setTelefono(dto.getTelefono());
        funcionario.setFechaNacimiento(dto.getFechaNacimiento());
        funcionario.setRol(rol);

        return funcionarioMapper.toResponseDTO(funcionarioRepository.save(funcionario));
    }

    @Transactional
    public void darDeBaja(Integer id) {
        Funcionario f = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario", id));

        if (!f.isActivo()) {
            throw new BusinessException("Ya está inactivo");
        }

        // Protección: no se puede dar de baja a ningún Administrador de Sistema
        if (f.getRol() != null && "ADMINISTRADOR_SISTEMA".equalsIgnoreCase(f.getRol().getNombre())) {
            throw new BusinessException("No se puede dar de baja a un Administrador de Sistema");
        }

        f.setActivo(false);
        f.setFechaBaja(LocalDate.now());
        funcionarioRepository.save(f);
    }

    @Transactional
    public CambiarContraseniaResponseDTO cambiarPassword(Integer id, CambiarContraseniaRequestDTO requestDTO) {
        String nuevaContrasenia = requestDTO.getNuevaContrasenia();

        if (nuevaContrasenia == null || nuevaContrasenia.length() < 8) {
            throw new BusinessException("La contraseña debe tener al menos 8 caracteres");
        }

        Funcionario funcionario = funcionarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Funcionario", id));

        funcionario.setContrasenia(passwordEncoder.encode(nuevaContrasenia));
        // Asegurarse de que el flag quede limpio si se usa este endpoint directamente
        funcionario.setMustChangePassword(false);

        funcionarioRepository.save(funcionario);

        return new CambiarContraseniaResponseDTO("Contraseña actualizada exitosamente");
    }

    @Transactional
    public void darDeAlta(Integer id) {
        Funcionario f = funcionarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Funcionario", id));

        if (f.isActivo()) {
            throw new BusinessException("Ya está activo");
        }

        f.setActivo(true);
        f.setFechaBaja(null);
        funcionarioRepository.save(f);
    }

    public FuncionarioResponseDTO obtenerPorId(Integer id) {
        return funcionarioMapper.toResponseDTO(
                funcionarioRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Funcionario", id))
        );
    }

    @Transactional
    public FuncionarioResponseDTO actualizarPerfil(Integer id, ActualizarPerfilRequestDTO dto) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario", id));

        if (funcionarioRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new BusinessException("Email ya en uso por otro usuario");
        }

        funcionario.setNombre(dto.getNombre());
        funcionario.setApellido(dto.getApellido());
        funcionario.setEmail(dto.getEmail());
        funcionario.setTelefono(dto.getTelefono());
        funcionario.setFechaNacimiento(dto.getFechaNacimiento());
        if (dto.getFotoPerfil() != null) {
            funcionario.setFotoPerfil(dto.getFotoPerfil());
        }

        return funcionarioMapper.toResponseDTO(funcionarioRepository.save(funcionario));
    }

    @Transactional
    public CambiarContraseniaResponseDTO blanquearPassword(Integer id, CambiarContraseniaRequestDTO dto) {
        String nuevaContrasenia = dto.getNuevaContrasenia();

        if (nuevaContrasenia == null || nuevaContrasenia.length() < 8) {
            throw new BusinessException("La contraseña temporal debe tener al menos 8 caracteres");
        }

        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario", id));

        if (!funcionario.isActivo()) {
            throw new BusinessException("No se puede blanquear la contraseña de un funcionario inactivo");
        }

        funcionario.setContrasenia(passwordEncoder.encode(nuevaContrasenia));
        funcionario.setMustChangePassword(true);
        funcionarioRepository.save(funcionario);

        return new CambiarContraseniaResponseDTO("Contraseña blanqueada. El funcionario deberá cambiarla al ingresar.");
    }

    @Transactional
    public CambiarContraseniaResponseDTO cambiarPasswordSeguro(Integer id, CambiarContraseniaSeguraRequestDTO dto) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario", id));

        if (!passwordEncoder.matches(dto.getContraseniaActual(), funcionario.getContrasenia())) {
            throw new BusinessException("La contraseña actual es incorrecta");
        }

        if (dto.getNuevaContrasenia() == null || dto.getNuevaContrasenia().length() < 8) {
            throw new BusinessException("La nueva contraseña debe tener al menos 8 caracteres");
        }

        funcionario.setContrasenia(passwordEncoder.encode(dto.getNuevaContrasenia()));
        funcionario.setMustChangePassword(false);
        funcionarioRepository.save(funcionario);

        return new CambiarContraseniaResponseDTO("Contraseña actualizada exitosamente");
    }


}