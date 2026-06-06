package tip.java.sistemacentrocrecer.biz.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tip.java.sistemacentrocrecer.biz.dao.entities.*;
import tip.java.sistemacentrocrecer.biz.dao.enums.EstadoInscripcionEnum;
import tip.java.sistemacentrocrecer.biz.dao.repositories.*;
import tip.java.sistemacentrocrecer.dto.*;
import tip.java.sistemacentrocrecer.exceptions.BusinessException;
import tip.java.sistemacentrocrecer.exceptions.ResourceNotFoundException;
import tip.java.sistemacentrocrecer.mapper.InscripcionMapper;
import tip.java.sistemacentrocrecer.mapper.InscripcionSolicitudMapper;
import tip.java.sistemacentrocrecer.mapper.RegistroCompletoMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InscripcionService {

    private final InscripcionRepository      inscripcionRepository;
    private final NinioRepository            ninioRepository;
    private final ResponsableRepository      responsableRepository;
    private final GrupoRepository            grupoRepository;
    private final ResponsableNinioRepository responsableNinioRepository;

    private final InscripcionMapper          inscripcionMapper;
    private final InscripcionSolicitudMapper solicitudMapper;
    private final RegistroCompletoMapper     registroMapper;

    private final PasswordEncoder            passwordEncoder;

    @Transactional(readOnly = true)
    public List<InscripcionResponseDTO> listarTodos() {
        return inscripcionRepository.findAll()
                .stream()
                .map(inscripcionMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public InscripcionResponseDTO obtenerPorId(Integer id) {
        return inscripcionMapper.toResponseDTO(
                findInscripcionOrThrow(id));
    }

    @Transactional
    public InscripcionResponseDTO crear(InscripcionRequestDTO dto) {
        Inscripcion inscripcion = inscripcionMapper.toEntity(dto);
        inscripcion.setNinio(findNinioOrThrow(dto.getNinioId()));
        inscripcion.setResponsables(List.of(findResponsableOrThrow(dto.getResponsableId())));
        return inscripcionMapper.toResponseDTO(inscripcionRepository.save(inscripcion));
    }

    @Transactional
    public InscripcionResponseDTO actualizar(Integer id, InscripcionRequestDTO dto) {
        Inscripcion inscripcion = findInscripcionOrThrow(id);
        inscripcion.setNinio(findNinioOrThrow(dto.getNinioId()));
        inscripcion.setResponsables(List.of(findResponsableOrThrow(dto.getResponsableId())));
        inscripcion.setFechaInscripcion(dto.getFechaInscripcion());
        inscripcion.setFechaInicio(dto.getFechaInicio());
        inscripcion.setFechaFin(dto.getFechaFin());
        inscripcion.setEstadoInscripcion(dto.getEstadoInscripcion());
        inscripcion.setObservaciones(dto.getObservaciones());
        return inscripcionMapper.toResponseDTO(inscripcionRepository.save(inscripcion));
    }

    @Transactional
    public void eliminar(Integer id) {
        inscripcionRepository.delete(findInscripcionOrThrow(id));
    }

    @Transactional
    public List<InscripcionSolicitudResponseDTO> registrarResponsableConNinos(
            RegistroCompletoRequestDTO dto) {

        validarUnicidadResponsable(dto.getCedula(), dto.getEmail());

        Responsable responsable = crearResponsable(dto);

        List<InscripcionSolicitudResponseDTO> resultado = new ArrayList<>();

        for (RegistroCompletoRequestDTO.NinioSolicitudDTO ninioDTO : dto.getNinos()) {
            Ninio ninio = obtenerOCrearNinio(ninioDTO);
            agregarCondicionesMedicas(ninio, ninioDTO);
            vincularResponsableNinio(responsable, ninio);
            validarSinInscripcionVigente(ninio);

            Inscripcion inscripcion = crearInscripcionPendiente(ninio, responsable);
            resultado.add(solicitudMapper.toDTO(inscripcion));
        }

        return resultado;
    }


    @Transactional(readOnly = true)
    public List<InscripcionSolicitudResponseDTO> listarPorResponsable(Integer responsableId) {
        List<Inscripcion> inscripciones = inscripcionRepository.findByResponsableId(responsableId);
        for (Inscripcion i : inscripciones) {
            if (i.getNinio() != null && i.getNinio().getCondiciones() != null) {
                i.getNinio().getCondiciones().size();
            }
            if (i.getResponsables() != null) i.getResponsables().size();
        }
        return solicitudMapper.toDTOList(inscripciones);
    }

    @Transactional
    public List<InscripcionSolicitudResponseDTO> solicitarNuevosNinos(
            Integer responsableId,
            List<RegistroCompletoRequestDTO.NinioSolicitudDTO> ninos) {

        Responsable responsable = findResponsableOrThrow(responsableId);
        List<InscripcionSolicitudResponseDTO> resultado = new ArrayList<>();

        for (RegistroCompletoRequestDTO.NinioSolicitudDTO ninioDTO : ninos) {
            Ninio ninio = obtenerOCrearNinio(ninioDTO);
            agregarCondicionesMedicas(ninio, ninioDTO);
            vincularResponsableNinio(responsable, ninio);
            validarSinInscripcionVigente(ninio);
            Inscripcion inscripcion = crearInscripcionPendiente(ninio, responsable);
            resultado.add(solicitudMapper.toDTO(inscripcion));
        }
        return resultado;
    }

    @Transactional(readOnly = true)
    public List<InscripcionSolicitudResponseDTO> listarPendientes() {
        List<Inscripcion> pendientes = inscripcionRepository
                .findByEstadoInscripcion(EstadoInscripcionEnum.PENDIENTE);

        // Forzar inicialización de colecciones lazy dentro de la transacción activa
        for (Inscripcion i : pendientes) {
            if (i.getNinio() != null) {
                if (i.getNinio().getCondiciones() != null) {
                    i.getNinio().getCondiciones().size();
                }
                // Fallback para responsable: inicializar ResponsableNinio del niño
                if (i.getNinio().getResponsables() != null) {
                    i.getNinio().getResponsables().size();
                }
            }
            if (i.getResponsables() != null) {
                i.getResponsables().size();
            }
        }

        return solicitudMapper.toDTOList(pendientes);
    }

    @Transactional
    public InscripcionSolicitudResponseDTO darDeAlta(Integer inscripcionId, DarDeAltaRequestDTO dto) {
        Inscripcion inscripcion = findInscripcionOrThrow(inscripcionId);

        if (inscripcion.getEstadoInscripcion() != EstadoInscripcionEnum.PENDIENTE) {
            throw new BusinessException("Solo se pueden dar de alta inscripciones en estado PENDIENTE");
        }

        Grupo grupo = grupoRepository.findById(dto.getGrupoId()).orElseThrow(() -> new ResourceNotFoundException("Grupo", dto.getGrupoId()));

        if (!grupo.isActivo()) {
            throw new BusinessException("El grupo seleccionado no está activo");
        }

        Ninio ninio = inscripcion.getNinio();
        ninio.setGrupo(grupo);
        ninioRepository.save(ninio);

        inscripcion.setEstadoInscripcion(EstadoInscripcionEnum.ACTIVA);
        inscripcion.setFechaInicio(LocalDate.now());
        if (dto.getObservaciones() != null) {
            inscripcion.setObservaciones(dto.getObservaciones());
        }

        return solicitudMapper.toDTO(inscripcionRepository.save(inscripcion));
    }

    @Transactional
    public void rechazar(Integer inscripcionId, RechazarInscripcionRequestDTO dto) {
        Inscripcion inscripcion = findInscripcionOrThrow(inscripcionId);

        if (inscripcion.getEstadoInscripcion() != EstadoInscripcionEnum.PENDIENTE) {
            throw new BusinessException("Solo se pueden rechazar inscripciones en estado PENDIENTE");
        }

        inscripcion.setEstadoInscripcion(EstadoInscripcionEnum.CANCELADA);
        inscripcion.setMotivoBaja(dto.getMotivo());
        inscripcion.setFechaBaja(LocalDateTime.now());
        inscripcionRepository.save(inscripcion);
    }

    private void validarUnicidadResponsable(String cedula, String email) {
        if (responsableRepository.findByCedula(cedula).isPresent()) {
            throw new BusinessException("Ya existe un responsable registrado con esa cédula");
        }
        if (responsableRepository.findByEmail(email).isPresent()) {
            throw new BusinessException("Ya existe un responsable registrado con ese email");
        }
    }

    private Responsable crearResponsable(RegistroCompletoRequestDTO dto) {
        Responsable responsable = Responsable.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .cedula(dto.getCedula())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .fechaNacimiento(dto.getFechaNacimiento())
                .contrasenia(passwordEncoder.encode(dto.getContrasenia()))
                .activo(true)
                .build();
        return responsableRepository.save(responsable);
    }

    private Ninio obtenerOCrearNinio(RegistroCompletoRequestDTO.NinioSolicitudDTO dto) {
        return ninioRepository.findByCedula(dto.getCedula())
                .orElseGet(() -> {
                    Ninio nuevo = registroMapper.toNinio(dto);
                    nuevo.setCondiciones(new ArrayList<>());
                    return ninioRepository.save(nuevo);
                });
    }

    private void agregarCondicionesMedicas(Ninio ninio, RegistroCompletoRequestDTO.NinioSolicitudDTO dto) {

        if (dto.getCondicionesMedicas() == null || dto.getCondicionesMedicas().isEmpty()) return;

        List<CondicionMedica> nuevas = registroMapper.toCondiciones(
                dto.getCondicionesMedicas(), ninio);

        if (ninio.getCondiciones() == null) ninio.setCondiciones(new ArrayList<>());
        ninio.getCondiciones().addAll(nuevas);
        ninioRepository.save(ninio);
    }

    private void vincularResponsableNinio(Responsable responsable, Ninio ninio) {
        boolean yaVinculado = responsableNinioRepository.findByNinioCedula(ninio.getCedula()).isPresent();
        if (!yaVinculado) {
            ResponsableNinio rn = ResponsableNinio.builder()
                    .responsable(responsable)
                    .ninio(ninio)
                    .tipoRelacion("RESPONSABLE")
                    .autorizadoRetiro(true)
                    .build();
            responsableNinioRepository.save(rn);
        }
    }

    private void validarSinInscripcionVigente(Ninio ninio) {
        boolean tieneVigente = inscripcionRepository.findByNinioId(ninio.getId())
                .stream()
                .anyMatch(i -> i.getEstadoInscripcion() == EstadoInscripcionEnum.PENDIENTE
                        || i.getEstadoInscripcion() == EstadoInscripcionEnum.ACTIVA);
        if (tieneVigente) {
            throw new BusinessException("El niño con cédula " + ninio.getCedula() + " ya tiene una inscripción pendiente o activa");
        }
    }

    private Inscripcion crearInscripcionPendiente(Ninio ninio, Responsable responsable) {
        Inscripcion inscripcion = Inscripcion.builder()
                .ninio(ninio)
                .fechaInscripcion(LocalDate.now())
                .estadoInscripcion(EstadoInscripcionEnum.PENDIENTE)
                .build();
        Inscripcion saved = inscripcionRepository.save(inscripcion);

        Responsable responsableFresh = responsableRepository.findById(responsable.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Responsable", responsable.getId()));
        if (responsableFresh.getInscripciones() == null) {
            responsableFresh.setInscripciones(new ArrayList<>());
        }
        responsableFresh.getInscripciones().add(saved);
        responsableRepository.save(responsableFresh);

        return saved;
    }

    private Inscripcion findInscripcionOrThrow(Integer id) {
        return inscripcionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Inscripción", id));
    }

    private Ninio findNinioOrThrow(Integer id) {
        return ninioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Niño", id));
    }

    private Responsable findResponsableOrThrow(Integer id) {
        return responsableRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Responsable", id));
    }
}