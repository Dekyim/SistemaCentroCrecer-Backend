package tip.java.sistemacentrocrecer.biz.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tip.java.sistemacentrocrecer.biz.dao.entities.Funcionario;
import tip.java.sistemacentrocrecer.biz.dao.entities.Grupo;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;
import tip.java.sistemacentrocrecer.biz.dao.repositories.FuncionarioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.GrupoRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.NinioRepository;
import tip.java.sistemacentrocrecer.dto.GrupoRequestDTO;
import tip.java.sistemacentrocrecer.dto.GrupoResponseDTO;
import tip.java.sistemacentrocrecer.exceptions.BusinessException;
import tip.java.sistemacentrocrecer.exceptions.ResourceNotFoundException;
import tip.java.sistemacentrocrecer.mapper.GrupoMapper;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class GrupoService {

    private static final LocalTime HORA_APERTURA_CENTRO = LocalTime.of(7, 0);
    private static final LocalTime HORA_CIERRE_CENTRO = LocalTime.of(19, 0);
    private static final String ROL_ADMINISTRADOR_SISTEMA = "ADMINISTRADOR_SISTEMA";

    private final GrupoRepository grupoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final NinioRepository ninioRepository;
    private final GrupoMapper grupoMapper;

    private void validarFuncionariosAsignables(List<Funcionario> funcionarios) {
        boolean hayAdministradorSistema = funcionarios.stream()
                .anyMatch(f -> f.getRol() != null && ROL_ADMINISTRADOR_SISTEMA.equalsIgnoreCase(f.getRol().getNombre()));
        if (hayAdministradorSistema) {
            throw new BusinessException("No se puede asignar un Administrador del Sistema a un grupo");
        }
    }

    private void validarHorario(LocalTime horaInicio, LocalTime horaFin) {
        if (horaInicio.isBefore(HORA_APERTURA_CENTRO) || horaInicio.isAfter(HORA_CIERRE_CENTRO)) {
            throw new BusinessException("La hora de inicio debe estar dentro del horario del centro (07:00 a 19:00)");
        }
        if (horaFin.isBefore(HORA_APERTURA_CENTRO) || horaFin.isAfter(HORA_CIERRE_CENTRO)) {
            throw new BusinessException("La hora de fin debe estar dentro del horario del centro (07:00 a 19:00)");
        }
        if (horaFin.isBefore(horaInicio)) {
            throw new BusinessException("La hora de fin no puede ser menor que la de inicio");
        }
    }

    @Transactional(readOnly = true)
    public List<GrupoResponseDTO> listarTodos() {
        return grupoRepository.findAll().stream()
                .map(grupoMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public GrupoResponseDTO buscarPorId(Integer id) {
        Grupo grupo = grupoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado"));
        return grupoMapper.toResponseDTO(grupo);
    }

    @Transactional(readOnly = true)
    public List<GrupoResponseDTO> listarGruposActivos() {
        return grupoRepository.findByActivoTrue().stream()
                .map(grupoMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public GrupoResponseDTO crear(GrupoRequestDTO dto) {
        validarHorario(dto.getHoraInicio(), dto.getHoraFin());

        Grupo grupo = grupoMapper.toEntity(dto);

        if (dto.getFuncionariosIds() != null) {
            List<Funcionario> funcionarios = funcionarioRepository.findAllById(dto.getFuncionariosIds());
            validarFuncionariosAsignables(funcionarios);
            grupo.setFuncionarios(funcionarios);
        }

        if (dto.getNiniosIds() != null) {
            List<Ninio> ninios = ninioRepository.findAllById(dto.getNiniosIds());
            grupo.setNinios(ninios);
            ninios.forEach(n -> n.setGrupo(grupo));
        }

        return grupoMapper.toResponseDTO(grupoRepository.save(grupo));
    }

    @Transactional
    public GrupoResponseDTO actualizar(Integer id, GrupoRequestDTO dto) {
        Grupo grupo = grupoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", id));

        validarHorario(dto.getHoraInicio(), dto.getHoraFin());

        grupo.setNombre(dto.getNombre());
        grupo.setHoraInicio(dto.getHoraInicio());
        grupo.setHoraFin(dto.getHoraFin());
        grupo.setRangoEdad(dto.getRangoEdad());

        if (dto.getFuncionariosIds() != null) {
            List<Funcionario> funcionarios = funcionarioRepository.findAllById(dto.getFuncionariosIds());
            validarFuncionariosAsignables(funcionarios);
            grupo.setFuncionarios(funcionarios);
        }

        if (dto.getNiniosIds() != null) {
            if (grupo.getNinios() != null) {
                grupo.getNinios().forEach(n -> n.setGrupo(null));
            }
            List<Ninio> ninios = ninioRepository.findAllById(dto.getNiniosIds());
            grupo.setNinios(ninios);
            ninios.forEach(n -> n.setGrupo(grupo));
        }

        return grupoMapper.toResponseDTO(grupoRepository.save(grupo));
    }

    @Transactional
    public void darDeBaja(Integer id) {
        Grupo g = grupoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", id));

        if (!g.isActivo()) {
            throw new BusinessException("El grupo ya está inactivo");
        }

        g.setActivo(false);
        g.setFechaBaja(LocalDateTime.now());
        grupoRepository.save(g);
    }
}