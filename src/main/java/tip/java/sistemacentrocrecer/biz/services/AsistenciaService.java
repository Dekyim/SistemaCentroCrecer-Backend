package tip.java.sistemacentrocrecer.biz.services;

import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tip.java.sistemacentrocrecer.biz.dao.entities.Asistencia;
import tip.java.sistemacentrocrecer.biz.dao.entities.Funcionario;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;
import tip.java.sistemacentrocrecer.biz.dao.repositories.AsistenciaRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.FuncionarioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.NinioRepository;
import tip.java.sistemacentrocrecer.dto.*;
import tip.java.sistemacentrocrecer.exceptions.BusinessException;
import tip.java.sistemacentrocrecer.exceptions.CedulaNotFoundException;
import tip.java.sistemacentrocrecer.exceptions.ResourceNotFoundException;
import tip.java.sistemacentrocrecer.mapper.AsistenciaMapper;
import tip.java.sistemacentrocrecer.mapper.NinioMapper;

import tip.java.sistemacentrocrecer.biz.dao.enums.EstadoPuntualidadEnum;
import tip.java.sistemacentrocrecer.biz.dao.repositories.TurnoRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class AsistenciaService {
    private final AsistenciaRepository asistenciaRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final NinioRepository ninioRepository;
    private final TurnoRepository turnoRepository;
    private final AsistenciaMapper asistenciaMapper;
    private final NinioMapper ninioMapper;
    private final ActividadService actividadService;
    private final CalendarioLaboralService calendarioLaboralService;

    private static final int TOLERANCIA_MINUTOS = 10;

    private EstadoPuntualidadEnum calcularEstadoEntrada(Integer funcionarioId, LocalDate fecha, LocalTime horaRegistrada) {
        DayOfWeek diaSemana = fecha.getDayOfWeek();
        List<tip.java.sistemacentrocrecer.biz.dao.entities.Turno> turnos =
                turnoRepository.findByFuncionarioIdAndActivoTrue(funcionarioId)
                        .stream()
                        .filter(t -> t.getDias().contains(diaSemana))
                        .toList();

        if (turnos.isEmpty()) return EstadoPuntualidadEnum.SIN_TURNO_ASIGNADO;

        // Buscar el turno más cercano (por hora de inicio) que aplica al día
        return turnos.stream()
                .map(t -> {
                    LocalTime inicio = t.getHoraInicio();
                    long diffMinutos = java.time.Duration.between(inicio, horaRegistrada).toMinutes();
                    if (diffMinutos <= TOLERANCIA_MINUTOS && diffMinutos >= -TOLERANCIA_MINUTOS) {
                        return EstadoPuntualidadEnum.EN_HORARIO;
                    } else if (horaRegistrada.isAfter(inicio.plusMinutes(TOLERANCIA_MINUTOS))) {
                        return EstadoPuntualidadEnum.TARDE;
                    } else {
                        return EstadoPuntualidadEnum.TEMPRANO;
                    }
                })
                // Si algún turno dice EN_HORARIO, priorizar ese resultado
                .min((a, b) -> {
                    if (a == EstadoPuntualidadEnum.EN_HORARIO) return -1;
                    if (b == EstadoPuntualidadEnum.EN_HORARIO) return 1;
                    return 0;
                })
                .orElse(EstadoPuntualidadEnum.SIN_TURNO_ASIGNADO);
    }

    private EstadoPuntualidadEnum calcularEstadoSalida(Integer funcionarioId, LocalDate fecha, LocalTime horaRegistrada) {
        DayOfWeek diaSemana = fecha.getDayOfWeek();
        List<tip.java.sistemacentrocrecer.biz.dao.entities.Turno> turnos =
                turnoRepository.findByFuncionarioIdAndActivoTrue(funcionarioId)
                        .stream()
                        .filter(t -> t.getDias().contains(diaSemana))
                        .toList();

        if (turnos.isEmpty()) return EstadoPuntualidadEnum.SIN_TURNO_ASIGNADO;

        return turnos.stream()
                .map(t -> {
                    LocalTime fin = t.getHoraFin();
                    long diffMinutos = java.time.Duration.between(fin, horaRegistrada).toMinutes();
                    if (diffMinutos <= TOLERANCIA_MINUTOS && diffMinutos >= -TOLERANCIA_MINUTOS) {
                        return EstadoPuntualidadEnum.EN_HORARIO;
                    } else if (horaRegistrada.isBefore(fin.minusMinutes(TOLERANCIA_MINUTOS))) {
                        return EstadoPuntualidadEnum.TEMPRANO;
                    } else {
                        return EstadoPuntualidadEnum.TARDE;
                    }
                })
                .min((a, b) -> {
                    if (a == EstadoPuntualidadEnum.EN_HORARIO) return -1;
                    if (b == EstadoPuntualidadEnum.EN_HORARIO) return 1;
                    return 0;
                })
                .orElse(EstadoPuntualidadEnum.SIN_TURNO_ASIGNADO);
    }



    public List<AsistenciaResponseDTO> listarTodos() {
        Funcionario funcionario = getFuncionarioAutenticado();
        return asistenciaRepository.findAccesiblesPorFuncionario(funcionario.getId()).stream()
                .map(asistenciaMapper::toResponseDTO)
                .toList();
    }

    public AsistenciaResponseDTO obtenerPorId(Integer id) {
        Asistencia a = asistenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asistencia", id));
        validarAccesoAAsistenciaDeNinio(a);
        return asistenciaMapper.toResponseDTO(a);
    }

    public AsistenciaResponseDTO obtenerPorCedula(String cedula) {
        Ninio ninio = ninioRepository.findByCedula(cedula)
                .orElseThrow(() -> new CedulaNotFoundException(cedula));
        validarAccesoANinio(ninio);

        Asistencia a = asistenciaRepository.findByNinio_Cedula(cedula)
                .orElseThrow(() -> new CedulaNotFoundException(cedula));
        return asistenciaMapper.toResponseDTO(a);
    }

    @Transactional
    public AsistenciaResponseDTO crear(AsistenciaRequestDTO dto) {
        if (dto.getHoraSalida() != null && dto.getHoraSalida().isBefore(dto.getHoraEntrada())) {
            throw new BusinessException("La hora de salida no puede ser menor que la de entrada");
        }
        Ninio ninio = ninioRepository.findById(dto.getNinioId())
                .orElseThrow(() -> new ResourceNotFoundException("Niño", dto.getNinioId()));
        validarAccesoANinio(ninio);

        validarDiaLaborableParaNinio(dto.getFecha());

        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario", dto.getFuncionarioId()));

        Asistencia asistencia = asistenciaMapper.toEntity(dto);
        asistencia.setNinio(ninio);
        asistencia.setFuncionario(funcionario);
        asistencia.setNinioNombre(ninio.getNombre());
        asistencia.setNinioCedula(ninio.getCedula());
        asistencia.setFuncionarioNombre(funcionario.getNombre());
        asistencia.setFuncionarioCedula(funcionario.getCedula());

        return asistenciaMapper.toResponseDTO(asistenciaRepository.save(asistencia));
    }

    @Transactional
    public void darDeBaja(Integer id) {
        Asistencia a = asistenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asistencia", id));
        validarAccesoAAsistenciaDeNinio(a);

        if (!a.getActivo()) {
            throw new BusinessException("La asistencia ya está inactiva");
        }
        a.setActivo(false);
        asistenciaRepository.save(a);
    }

    private Funcionario getFuncionarioAutenticado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return funcionarioRepository.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("El usuario autenticado no es un funcionario autorizado"));
    }

    private void validarAccesoANinio(Ninio ninio) {
        Funcionario funcionario = getFuncionarioAutenticado();
        boolean tieneAcceso = asistenciaRepository.ninioPerteneceFuncionario(ninio.getId(), funcionario.getId());

        if (!tieneAcceso) {
            throw new AccessDeniedException("No tiene permisos para consultar o modificar asistencias de este niño");
        }
    }

    private void validarAccesoAAsistenciaDeNinio(Asistencia asistencia) {
        if (asistencia.getNinio() != null) {
            validarAccesoANinio(asistencia.getNinio());
        }
    }

    private void validarFechaEsHoy(LocalDate fecha) {
        if (fecha != null && fecha.isAfter(LocalDate.now())) {
            throw new BusinessException("No se puede registrar asistencia para una fecha futura");
        }
        if (fecha != null && fecha.isBefore(LocalDate.now())) {
            throw new BusinessException("No se puede registrar asistencia fuera de fecha. Solo se permite marcar asistencia del día de hoy");
        }
    }

    private void validarDiaLaborableParaNinio(LocalDate fecha) {
        if (fecha == null) {
            throw new BusinessException("La fecha de asistencia es obligatoria");
        }

        if (!esDiaDeSemana(fecha) || calendarioLaboralService.esDiaNoLaborable(fecha)) {
            throw new BusinessException("No se puede registrar asistencia de niños en un día no laborable");
        }
    }

    @Transactional
    public AsistenciaResponseDTO registrarEntradaPropia(RegistroEntradaFuncionarioRequestDTO dto) {
        Funcionario funcionario = getFuncionarioAutenticado();

        LocalDate fecha = dto.getFecha() != null ? dto.getFecha() : LocalDate.now();
        validarFechaEsHoy(fecha);

        boolean yaRegistrado = asistenciaRepository
                .findByFuncionario_Id(funcionario.getId())
                .stream()
                .anyMatch(a -> a.getFecha().equals(fecha) && Boolean.TRUE.equals(a.getActivo()) && a.getNinio() == null);

        if (yaRegistrado) {
            throw new BusinessException("Ya existe un registro de entrada para el día " + fecha);
        }

        LocalTime horaEntrada = dto.getHoraEntrada() != null ? dto.getHoraEntrada() : LocalTime.now();

        if (dto.getHoraSalida() != null && dto.getHoraSalida().isBefore(horaEntrada)) {
            throw new BusinessException("La hora de salida no puede ser anterior a la hora de entrada");
        }

        Asistencia asistencia = new Asistencia();
        asistencia.setFecha(fecha);
        asistencia.setHoraEntrada(horaEntrada);
        asistencia.setHoraSalida(dto.getHoraSalida());
        asistencia.setObservaciones(dto.getObservaciones());
        asistencia.setActivo(true);
        asistencia.setFuncionario(funcionario);
        asistencia.setFuncionarioNombre(funcionario.getNombre() + " " + funcionario.getApellido());
        asistencia.setFuncionarioCedula(funcionario.getCedula());

        AsistenciaResponseDTO respuesta = asistenciaMapper.toResponseDTO(asistenciaRepository.save(asistencia));
        respuesta.setEstadoEntrada(calcularEstadoEntrada(funcionario.getId(), fecha, horaEntrada));
        if (dto.getHoraSalida() != null) {
            respuesta.setEstadoSalida(calcularEstadoSalida(funcionario.getId(), fecha, dto.getHoraSalida()));
        }
        return respuesta;
    }

    @Transactional
    public AsistenciaResponseDTO registrarSalidaPropia(RegistroSalidaFuncionarioRequestDTO dto) {
        Funcionario funcionario = getFuncionarioAutenticado();

        LocalDate fechaBusqueda = dto.getFecha() != null ? dto.getFecha() : LocalDate.now();
        validarFechaEsHoy(fechaBusqueda);

        Asistencia asistencia = asistenciaRepository
                .findByFuncionario_Id(funcionario.getId())
                .stream()
                .filter(a -> a.getFecha().equals(fechaBusqueda) && Boolean.TRUE.equals(a.getActivo()) && a.getNinio() == null)
                .findFirst()
                .orElseThrow(() -> new BusinessException("No hay registro de entrada para hoy. Registre su entrada primero."));

        if (asistencia.getHoraSalida() != null) {
            throw new BusinessException("Ya existe una hora de salida registrada para hoy");
        }

        LocalTime horaSalidaFinal = dto.getHoraSalida() != null ? dto.getHoraSalida() : LocalTime.now();
        if (horaSalidaFinal.isBefore(asistencia.getHoraEntrada())) {
            throw new BusinessException("La hora de salida no puede ser anterior a la hora de entrada (" + asistencia.getHoraEntrada() + ")");
        }

        asistencia.setHoraSalida(horaSalidaFinal);
        if (dto.getObservaciones() != null && !dto.getObservaciones().isBlank()) {
            String obsActual = asistencia.getObservaciones();
            asistencia.setObservaciones(
                    obsActual != null && !obsActual.isBlank()
                            ? obsActual + " | " + dto.getObservaciones().trim()
                            : dto.getObservaciones().trim()
            );
        }
        AsistenciaResponseDTO respuesta = asistenciaMapper.toResponseDTO(asistenciaRepository.save(asistencia));
        respuesta.setEstadoEntrada(calcularEstadoEntrada(funcionario.getId(), asistencia.getFecha(), asistencia.getHoraEntrada()));
        respuesta.setEstadoSalida(calcularEstadoSalida(funcionario.getId(), asistencia.getFecha(), horaSalidaFinal));
        return respuesta;
    }

    public AsistenciaResponseDTO obtenerMiRegistroDelDia(LocalDate fecha) {
        Funcionario funcionario = getFuncionarioAutenticado();
        LocalDate fechaBusqueda = fecha != null ? fecha : LocalDate.now();

        return asistenciaRepository
                .findByFuncionario_Id(funcionario.getId())
                .stream()
                .filter(a -> a.getFecha().equals(fechaBusqueda) && a.getNinio() == null)
                .findFirst()
                .map(a -> {
                    AsistenciaResponseDTO dto = asistenciaMapper.toResponseDTO(a);
                    dto.setEstadoEntrada(calcularEstadoEntrada(funcionario.getId(), a.getFecha(), a.getHoraEntrada()));
                    if (a.getHoraSalida() != null) {
                        dto.setEstadoSalida(calcularEstadoSalida(funcionario.getId(), a.getFecha(), a.getHoraSalida()));
                    }
                    return dto;
                })
                .orElse(null);
    }

    @Transactional
    public AsistenciaResponseDTO marcarAsistenciaNinio(AsistenciaNinioRequestDTO dto) {
        Funcionario funcionario = getFuncionarioAutenticado();

        Ninio ninio = ninioRepository.findById(dto.getNinioId())
                .orElseThrow(() -> new ResourceNotFoundException("Niño", dto.getNinioId()));

        validarAccesoANinio(ninio);

        if (!ninio.isActivo()) {
            throw new BusinessException("El niño no está activo en el sistema");
        }

        LocalDate fecha = dto.getFecha() != null ? dto.getFecha() : LocalDate.now();
        validarFechaEsHoy(fecha);
        validarDiaLaborableParaNinio(fecha);

        actividadService.validarPermisoParaActividadDelDia(
                dto.getActividadId(), ninio.getId(), fecha
        );

        if (asistenciaRepository.existsByNinio_IdAndFechaAndActivoTrue(ninio.getId(), fecha)) {
            throw new BusinessException("Ya se registró asistencia para el niño " + ninio.getNombre() + " el día " + fecha);
        }

        if (dto.getHoraSalida() != null && dto.getHoraSalida().isBefore(dto.getHoraEntrada())) {
            throw new BusinessException("La hora de salida no puede ser anterior a la hora de entrada");
        }

        Asistencia asistencia = asistenciaMapper.toEntityFromNinio(dto);
        asistencia.setFecha(fecha);
        asistencia.setNinio(ninio);
        asistencia.setFuncionario(funcionario);
        asistencia.setNinioNombre(ninio.getNombre());
        asistencia.setNinioCedula(ninio.getCedula());
        asistencia.setFuncionarioNombre(funcionario.getNombre() + " " + funcionario.getApellido());
        asistencia.setFuncionarioCedula(funcionario.getCedula());

        return asistenciaMapper.toResponseDTO(asistenciaRepository.save(asistencia));
    }

    public List<AsistenciaResponseDTO> listarAsistenciasDeNinosPorFecha(LocalDate fecha) {
        Funcionario funcionario = getFuncionarioAutenticado();
        LocalDate fechaBusqueda = fecha != null ? fecha : LocalDate.now();
        return asistenciaRepository
                .findAsistenciasPorFuncionarioFecha(funcionario.getId(), fechaBusqueda)
                .stream()
                .map(asistenciaMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public AsistenciaResponseDTO registrarSalidaNinio(Integer asistenciaId, RegistroSalidaNinioRequestDTO dto) {
        Asistencia asistencia = asistenciaRepository.findById(asistenciaId)
                .orElseThrow(() -> new ResourceNotFoundException("Asistencia", asistenciaId));

        if (asistencia.getNinio() == null) {
            throw new BusinessException("El registro no corresponde a un niño");
        }

        validarAccesoANinio(asistencia.getNinio());
        validarFechaEsHoy(asistencia.getFecha());

        if (asistencia.getHoraSalida() != null) {
            throw new BusinessException("Ya existe una hora de salida registrada para este niño");
        }

        LocalTime horaSalidaFinal = dto.getHoraSalida() != null ? dto.getHoraSalida() : LocalTime.now();
        if (horaSalidaFinal.isBefore(asistencia.getHoraEntrada())) {
            throw new BusinessException("La hora de salida no puede ser anterior a la hora de entrada (" + asistencia.getHoraEntrada() + ")");
        }

        asistencia.setHoraSalida(horaSalidaFinal);
        if (dto.getObservaciones() != null && !dto.getObservaciones().isBlank()) {
            String obsActual = asistencia.getObservaciones();
            asistencia.setObservaciones(
                    obsActual != null && !obsActual.isBlank()
                            ? obsActual + " | " + dto.getObservaciones().trim()
                            : dto.getObservaciones().trim()
            );
        }
        return asistenciaMapper.toResponseDTO(asistenciaRepository.save(asistencia));
    }

    public List<AsistenciaResponseDTO> listarAsistenciasPorNinios(List<Integer> ninioIds, LocalDate fecha) {
        getFuncionarioAutenticado();

        if (ninioIds == null || ninioIds.isEmpty()) {
            return List.of();
        }

        for (Integer ninioId : ninioIds) {
            Ninio ninio = ninioRepository.findById(ninioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Niño", ninioId));
            validarAccesoANinio(ninio);
        }

        LocalDate fechaBusqueda = fecha != null ? fecha : LocalDate.now();
        return asistenciaRepository.findByNinio_IdInAndFechaAndActivoTrue(ninioIds, fechaBusqueda)
                .stream()
                .map(asistenciaMapper::toResponseDTO)
                .toList();
    }

    public List<NinioResponseDTO> listarNiniosDeMisGrupos() {
        Funcionario funcionario = getFuncionarioAutenticado();
        return ninioMapper.toDTOList(
                ninioRepository.findNiniosByFuncionarioId(funcionario.getId())
        );
    }

    public List<AsistenciaResponseDTO> historialPorCedula(String cedula) {
        Ninio ninio = ninioRepository.findByCedula(cedula)
                .orElseThrow(() -> new CedulaNotFoundException(cedula));
        validarAccesoANinio(ninio);

        return asistenciaRepository.findHistorialPorCedulaNinio(cedula)
                .stream()
                .map(asistenciaMapper::toResponseDTO)
                .toList();
    }


    public FrecuenciaAsistenciaResponseDTO frecuenciaPorCedula(String cedula, LocalDate desde, LocalDate hasta) {
        if (hasta.isBefore(desde)) {
            throw new BusinessException("La fecha 'hasta' no puede ser anterior a 'desde'");
        }

        Ninio ninio = ninioRepository.findByCedula(cedula)
                .orElseThrow(() -> new CedulaNotFoundException(cedula));
        validarAccesoANinio(ninio);

        Set<LocalDate> fechasNoLaborables =
                calendarioLaboralService.obtenerFechasNoLaborables(desde, hasta);

        long diasPresente = asistenciaRepository
                .findByNinio_IdAndFechaBetweenAndActivoTrueOrderByFechaDesc(
                        ninio.getId(), desde, hasta
                )
                .stream()
                .map(Asistencia::getFecha)
                .distinct()
                .filter(this::esDiaDeSemana)
                .filter(fecha -> !fechasNoLaborables.contains(fecha))
                .count();

        long totalDiasHabiles = calendarioLaboralService.contarDiasHabiles(desde, hasta);

        long diasAusente = totalDiasHabiles - diasPresente;
        if (diasAusente < 0) diasAusente = 0;

        double pctAsistencia = totalDiasHabiles > 0
                ? Math.round((diasPresente * 100.0 / totalDiasHabiles) * 10.0) / 10.0 : 0.0;
        double pctInasistencia = totalDiasHabiles > 0
                ? Math.round((diasAusente * 100.0 / totalDiasHabiles) * 10.0) / 10.0 : 0.0;

        FrecuenciaAsistenciaResponseDTO dto = new FrecuenciaAsistenciaResponseDTO();
        dto.setNinioId(ninio.getId());
        dto.setNinioNombre(ninio.getNombre());
        dto.setNinioApellido(ninio.getApellido());
        dto.setNinioCedula(ninio.getCedula());
        dto.setGrupoNombre(ninio.getGrupo() != null ? ninio.getGrupo().getNombre() : null);
        dto.setDesde(desde);
        dto.setHasta(hasta);
        dto.setDiasPresente(diasPresente);
        dto.setDiasAusente(diasAusente);
        dto.setTotalDiasHabiles(totalDiasHabiles);
        dto.setPorcentajeAsistencia(pctAsistencia);
        dto.setPorcentajeInasistencia(pctInasistencia);
        return dto;
    }

    private boolean esDiaDeSemana(LocalDate fecha) {
        return fecha.getDayOfWeek() != DayOfWeek.SATURDAY &&
                fecha.getDayOfWeek() != DayOfWeek.SUNDAY;
    }

    public List<AsistenciaResponseDTO> listarAsistenciasFuncionariosPorRango(LocalDate desde, LocalDate hasta) {
        if (hasta.isBefore(desde)) {
            throw new BusinessException("La fecha 'hasta' no puede ser anterior a 'desde'");
        }
        return asistenciaRepository.findAsistenciasFuncionariosPorRango(desde, hasta)
                .stream()
                .map(a -> {
                    AsistenciaResponseDTO dto = asistenciaMapper.toResponseDTO(a);
                    dto.setEstadoEntrada(calcularEstadoEntrada(a.getFuncionario().getId(), a.getFecha(), a.getHoraEntrada()));
                    if (a.getHoraSalida() != null) {
                        dto.setEstadoSalida(calcularEstadoSalida(a.getFuncionario().getId(), a.getFecha(), a.getHoraSalida()));
                    }
                    return dto;
                })
                .toList();
    }
}
