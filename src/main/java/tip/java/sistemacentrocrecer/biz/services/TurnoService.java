package tip.java.sistemacentrocrecer.biz.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.entities.Funcionario;
import tip.java.sistemacentrocrecer.biz.dao.entities.Turno;
import tip.java.sistemacentrocrecer.biz.dao.repositories.FuncionarioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.TurnoRepository;
import tip.java.sistemacentrocrecer.dto.TurnoRequestDTO;
import tip.java.sistemacentrocrecer.dto.TurnoResponseDTO;
import tip.java.sistemacentrocrecer.mapper.TurnoMapper;

import java.time.LocalDateTime;
import java.util.List;

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
}