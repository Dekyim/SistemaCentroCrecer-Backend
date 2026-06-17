package tip.java.sistemacentrocrecer.biz.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.entities.Funcionario;
import tip.java.sistemacentrocrecer.biz.dao.entities.Turno;
import tip.java.sistemacentrocrecer.biz.dao.repositories.FuncionarioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.TurnoRepository;
import tip.java.sistemacentrocrecer.dto.TurnoRequestDTO;
import tip.java.sistemacentrocrecer.dto.TurnoResponseDTO;
import tip.java.sistemacentrocrecer.exceptions.BusinessException;
import tip.java.sistemacentrocrecer.mapper.TurnoMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final TurnoMapper turnoMapper;

    public List<TurnoResponseDTO> listarTodos() {
        return turnoRepository.findAll()
                .stream()
                .map(turnoMapper::toResponseDTO)
                .toList();
    }

    public List<TurnoResponseDTO> listarVisibles() {
        Funcionario solicitante = getFuncionarioAutenticado();
        String rolNombre = solicitante.getRol() != null ? solicitante.getRol().getNombre() : "";

        boolean esAdminOCoord = rolNombre.equals("ADMINISTRADOR_SISTEMA") || rolNombre.equals("COORDINADORA");

        if (esAdminOCoord) {
            return turnoRepository.findAll()
                    .stream()
                    .map(turnoMapper::toResponseDTO)
                    .toList();
        }

        List<Turno> propios = turnoRepository.findByFuncionarioId(solicitante.getId());
        List<Turno> deCoordinadoras = turnoRepository.findByFuncionario_Rol_NombreIgnoreCase("COORDINADORA");

        return Stream.concat(propios.stream(), deCoordinadoras.stream())
                .distinct()
                .map(turnoMapper::toResponseDTO)
                .toList();
    }

    public List<TurnoResponseDTO> listarActivos() {
        return turnoRepository.findByActivoTrue()
                .stream()
                .map(turnoMapper::toResponseDTO)
                .toList();
    }

    public TurnoResponseDTO obtenerPorId(Integer id) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado con id: " + id));
        return turnoMapper.toResponseDTO(turno);
    }

    @Transactional
    public TurnoResponseDTO crear(TurnoRequestDTO dto) {
        Turno turno = turnoMapper.toEntity(dto);

        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new RuntimeException("Funcionario no encontrado con id: " + dto.getFuncionarioId()));

        if (!funcionario.isActivo()) {
            throw new RuntimeException("No se puede asignar un turno a un funcionario inactivo");
        }

        turno.setFuncionario(funcionario);
        turno.setActivo(true);

        if (dto.getDias() != null) {
            turno.setDias(dto.getDias());
        }

        return turnoMapper.toResponseDTO(turnoRepository.save(turno));
    }

    @Transactional
    public TurnoResponseDTO actualizar(Integer id, TurnoRequestDTO dto) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado con id: " + id));

        turno.setHoraInicio(dto.getHoraInicio());
        turno.setHoraFin(dto.getHoraFin());

        if (dto.getDias() != null) {
            turno.getDias().clear();
            turno.getDias().addAll(dto.getDias());
        }

        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new RuntimeException("Funcionario no encontrado con id: " + dto.getFuncionarioId()));
        turno.setFuncionario(funcionario);

        return turnoMapper.toResponseDTO(turnoRepository.save(turno));
    }

    @Transactional
    public void darDeBaja(Integer id) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado con id: " + id));

        if (!turno.getActivo()) {
            throw new IllegalStateException("El turno ya está dado de baja");
        }

        turno.setActivo(false);
        turno.setFechaBaja(LocalDateTime.now());

        turnoRepository.save(turno);
    }

    @Transactional
    public TurnoResponseDTO reactivar(Integer id) {
        Funcionario solicitante = getFuncionarioAutenticado();

        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado con id: " + id));

        if (turno.getActivo()) {
            throw new BusinessException("El turno ya está activo");
        }

        boolean esAdminOCoord = solicitante.getRol() != null &&
                (solicitante.getRol().getNombre().equals("ADMINISTRADOR_SISTEMA") ||
                        solicitante.getRol().getNombre().equals("COORDINADORA"));

        if (!esAdminOCoord && !turno.getFuncionario().getId().equals(solicitante.getId())) {
            throw new BusinessException("Solo podés dar de alta tus propios turnos");
        }

        turno.setActivo(true);
        turno.setFechaBaja(null);

        return turnoMapper.toResponseDTO(turnoRepository.save(turno));
    }

    private Funcionario getFuncionarioAutenticado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return funcionarioRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Funcionario autenticado no encontrado"));
    }
}
