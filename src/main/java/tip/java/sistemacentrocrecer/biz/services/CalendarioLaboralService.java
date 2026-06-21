package tip.java.sistemacentrocrecer.biz.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tip.java.sistemacentrocrecer.biz.dao.entities.DiaNoLaborable;
import tip.java.sistemacentrocrecer.biz.dao.entities.Funcionario;
import tip.java.sistemacentrocrecer.biz.dao.repositories.DiaNoLaborableRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.FuncionarioRepository;
import tip.java.sistemacentrocrecer.dto.DiaNoLaborableRequestDTO;
import tip.java.sistemacentrocrecer.dto.DiaNoLaborableResponseDTO;
import tip.java.sistemacentrocrecer.exceptions.BusinessException;
import tip.java.sistemacentrocrecer.exceptions.ResourceNotFoundException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CalendarioLaboralService {

    private final DiaNoLaborableRepository diaNoLaborableRepository;
    private final FuncionarioRepository funcionarioRepository;

    @Transactional(readOnly = true)
    public List<DiaNoLaborableResponseDTO> listar(LocalDate desde, LocalDate hasta) {
        validarRango(desde, hasta);

        return diaNoLaborableRepository
                .findByFechaBetweenAndActivoTrueOrderByFechaAsc(desde, hasta)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public DiaNoLaborableResponseDTO crear(DiaNoLaborableRequestDTO dto) {
        if (diaNoLaborableRepository.existsByFechaAndActivoTrue(dto.getFecha())) {
            throw new BusinessException("Ya existe un día no laborable activo para esa fecha");
        }

        DiaNoLaborable dia = DiaNoLaborable.builder()
                .fecha(dto.getFecha())
                .motivo(dto.getMotivo().trim())
                .tipo(dto.getTipo())
                .activo(true)
                .creadoPor(getAdministradorAutenticado())
                .build();

        return toDTO(diaNoLaborableRepository.save(dia));
    }

    @Transactional
    public DiaNoLaborableResponseDTO actualizar(Integer id, DiaNoLaborableRequestDTO dto) {
        DiaNoLaborable dia = buscarActivo(id);

        if (diaNoLaborableRepository.existsByFechaAndActivoTrueAndIdNot(dto.getFecha(), id)) {
            throw new BusinessException("Ya existe un día no laborable activo para esa fecha");
        }

        dia.setFecha(dto.getFecha());
        dia.setMotivo(dto.getMotivo().trim());
        dia.setTipo(dto.getTipo());

        return toDTO(diaNoLaborableRepository.save(dia));
    }

    @Transactional
    public void darDeBaja(Integer id) {
        DiaNoLaborable dia = buscarActivo(id);
        dia.setActivo(false);
        dia.setFechaBaja(LocalDateTime.now());
        diaNoLaborableRepository.save(dia);
    }

    @Transactional(readOnly = true)
    public boolean esDiaNoLaborable(LocalDate fecha) {
        return diaNoLaborableRepository.existsByFechaAndActivoTrue(fecha);
    }

    @Transactional(readOnly = true)
    public long contarDiasHabiles(LocalDate desde, LocalDate hasta) {
        validarRango(desde, hasta);

        Set<LocalDate> noLaborables = obtenerFechasNoLaborables(desde, hasta);

        return desde.datesUntil(hasta.plusDays(1))
                .filter(this::esDiaDeSemana)
                .filter(fecha -> !noLaborables.contains(fecha))
                .count();
    }

    @Transactional(readOnly = true)
    public Set<LocalDate> obtenerFechasNoLaborables(LocalDate desde, LocalDate hasta) {
        validarRango(desde, hasta);

        return new HashSet<>(
                diaNoLaborableRepository
                        .findByFechaBetweenAndActivoTrueOrderByFechaAsc(desde, hasta)
                        .stream()
                        .map(DiaNoLaborable::getFecha)
                        .toList()
        );
    }

    private boolean esDiaDeSemana(LocalDate fecha) {
        return fecha.getDayOfWeek() != DayOfWeek.SATURDAY &&
                fecha.getDayOfWeek() != DayOfWeek.SUNDAY;
    }

    private DiaNoLaborable buscarActivo(Integer id) {
        DiaNoLaborable dia = diaNoLaborableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Día no laborable", id));

        if (!Boolean.TRUE.equals(dia.getActivo())) {
            throw new BusinessException("El día no laborable ya está dado de baja");
        }

        return dia;
    }

    private Funcionario getAdministradorAutenticado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Funcionario funcionario = funcionarioRepository.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("Administrador autenticado no encontrado"));

        String rol = funcionario.getRol() != null ? funcionario.getRol().getNombre() : "";
        if (!"ADMIN".equalsIgnoreCase(rol) &&
                !"ADMINISTRADOR_SISTEMA".equalsIgnoreCase(rol)) {
            throw new AccessDeniedException("Solo un administrador puede modificar el calendario laboral");
        }

        return funcionario;
    }

    private void validarRango(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            throw new BusinessException("Las fechas desde y hasta son obligatorias");
        }
        if (hasta.isBefore(desde)) {
            throw new BusinessException("La fecha hasta no puede ser anterior a desde");
        }
    }

    private DiaNoLaborableResponseDTO toDTO(DiaNoLaborable dia) {
        DiaNoLaborableResponseDTO dto = new DiaNoLaborableResponseDTO();
        dto.setId(dia.getId());
        dto.setFecha(dia.getFecha());
        dto.setMotivo(dia.getMotivo());
        dto.setTipo(dia.getTipo());
        dto.setActivo(dia.getActivo());

        if (dia.getCreadoPor() != null) {
            dto.setCreadoPorId(dia.getCreadoPor().getId());
            dto.setCreadoPorNombre(
                    dia.getCreadoPor().getNombre() + " " + dia.getCreadoPor().getApellido()
            );
        }

        return dto;
    }
}
