package tip.java.sistemacentrocrecer.biz.services;

import lombok.AllArgsConstructor;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AsistenciaService {
    private final AsistenciaRepository asistenciaRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final NinioRepository ninioRepository;
    private final AsistenciaMapper asistenciaMapper;
    private final NinioMapper ninioMapper;

    // ─── Métodos existentes ───────────────────────────────────────────────────

    public List<AsistenciaResponseDTO> listarTodos() {
        return asistenciaRepository.findAll().stream()
                .map(asistenciaMapper::toResponseDTO)
                .toList();
    }

    public AsistenciaResponseDTO obtenerPorId(Integer id) {
        Asistencia a = asistenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asistencia", id));
        return asistenciaMapper.toResponseDTO(a);
    }

    public AsistenciaResponseDTO obtenerPorCedula(String cedula) {
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
        if (!a.getActivo()) {
            throw new BusinessException("La asistencia ya está inactiva");
        }
        a.setActivo(false);
        asistenciaRepository.save(a);
    }

    // ─── Métodos con restricciones de seguridad ───────────────────────────────

    private Funcionario getFuncionarioAutenticado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return funcionarioRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Funcionario autenticado no encontrado"));
    }

    @Transactional
    public AsistenciaResponseDTO registrarEntradaPropia(RegistroEntradaFuncionarioRequestDTO dto) {
        Funcionario funcionario = getFuncionarioAutenticado();

        LocalDate fecha = dto.getFecha() != null ? dto.getFecha() : LocalDate.now();

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

        return asistenciaMapper.toResponseDTO(asistenciaRepository.save(asistencia));
    }

    @Transactional
    public AsistenciaResponseDTO registrarSalidaPropia(LocalDate fecha, LocalTime horaSalida) {
        Funcionario funcionario = getFuncionarioAutenticado();

        LocalDate fechaBusqueda = fecha != null ? fecha : LocalDate.now();

        Asistencia asistencia = asistenciaRepository
                .findByFuncionario_Id(funcionario.getId())
                .stream()
                .filter(a -> a.getFecha().equals(fechaBusqueda) && Boolean.TRUE.equals(a.getActivo()) && a.getNinio() == null)
                .findFirst()
                .orElseThrow(() -> new BusinessException("No hay registro de entrada para hoy. Registre su entrada primero."));

        if (asistencia.getHoraSalida() != null) {
            throw new BusinessException("Ya existe una hora de salida registrada para hoy");
        }

        LocalTime horaSalidaFinal = horaSalida != null ? horaSalida : LocalTime.now();
        if (horaSalidaFinal.isBefore(asistencia.getHoraEntrada())) {
            throw new BusinessException("La hora de salida no puede ser anterior a la hora de entrada (" + asistencia.getHoraEntrada() + ")");
        }

        asistencia.setHoraSalida(horaSalidaFinal);
        return asistenciaMapper.toResponseDTO(asistenciaRepository.save(asistencia));
    }

    public AsistenciaResponseDTO obtenerMiRegistroDelDia(LocalDate fecha) {
        Funcionario funcionario = getFuncionarioAutenticado();
        LocalDate fechaBusqueda = fecha != null ? fecha : LocalDate.now();

        return asistenciaRepository
                .findByFuncionario_Id(funcionario.getId())
                .stream()
                .filter(a -> a.getFecha().equals(fechaBusqueda) && a.getNinio() == null)
                .findFirst()
                .map(asistenciaMapper::toResponseDTO)
                .orElse(null);
    }

    @Transactional
    public AsistenciaResponseDTO marcarAsistenciaNinio(AsistenciaNinioRequestDTO dto) {
        Funcionario funcionario = getFuncionarioAutenticado();

        Ninio ninio = ninioRepository.findById(dto.getNinioId())
                .orElseThrow(() -> new ResourceNotFoundException("Niño", dto.getNinioId()));

        if (!ninio.isActivo()) {
            throw new BusinessException("El niño no está activo en el sistema");
        }

        boolean tieneAcceso = asistenciaRepository.ninioPerteneceFuncionario(ninio.getId(), funcionario.getId());
        if (!tieneAcceso) {
            throw new BusinessException("No tiene permisos para marcar asistencia de este niño. Solo puede marcar asistencia de niños de sus grupos.");
        }

        LocalDate fecha = dto.getFecha() != null ? dto.getFecha() : LocalDate.now();

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
    public AsistenciaResponseDTO registrarSalidaNinio(Integer asistenciaId, LocalTime horaSalida) {
        Funcionario funcionario = getFuncionarioAutenticado();

        Asistencia asistencia = asistenciaRepository.findById(asistenciaId)
                .orElseThrow(() -> new ResourceNotFoundException("Asistencia", asistenciaId));

        if (asistencia.getNinio() == null) {
            throw new BusinessException("El registro no corresponde a un niño");
        }

        boolean tieneAcceso = asistenciaRepository.ninioPerteneceFuncionario(
                asistencia.getNinio().getId(), funcionario.getId());
        if (!tieneAcceso) {
            throw new BusinessException("No tiene permisos para modificar la asistencia de este niño");
        }

        if (asistencia.getHoraSalida() != null) {
            throw new BusinessException("Ya existe una hora de salida registrada para este niño");
        }

        LocalTime horaSalidaFinal = horaSalida != null ? horaSalida : LocalTime.now();
        if (horaSalidaFinal.isBefore(asistencia.getHoraEntrada())) {
            throw new BusinessException("La hora de salida no puede ser anterior a la hora de entrada (" + asistencia.getHoraEntrada() + ")");
        }

        asistencia.setHoraSalida(horaSalidaFinal);
        return asistenciaMapper.toResponseDTO(asistenciaRepository.save(asistencia));
    }

    public List<NinioResponseDTO> listarNiniosDeMisGrupos() {
        Funcionario funcionario = getFuncionarioAutenticado();
        return ninioMapper.toDTOList(
                ninioRepository.findNiniosByFuncionarioId(funcionario.getId())
        );
    }
}