package tip.java.sistemacentrocrecer.biz.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tip.java.sistemacentrocrecer.biz.dao.entities.Actividad;
import tip.java.sistemacentrocrecer.biz.dao.entities.EmpresaExterna;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;
import tip.java.sistemacentrocrecer.biz.dao.entities.Permiso;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ActividadRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.EmpresaExternaRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.NinioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.PermisoRepository;
import tip.java.sistemacentrocrecer.dto.ActividadRequestDTO;
import tip.java.sistemacentrocrecer.dto.ActividadResponseDTO;
import tip.java.sistemacentrocrecer.exceptions.BusinessException;
import tip.java.sistemacentrocrecer.exceptions.ResourceNotFoundException;
import tip.java.sistemacentrocrecer.mapper.ActividadMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ActividadService {
    private final ActividadRepository actividadRepository;
    private final NinioRepository ninioRepository;
    private final PermisoRepository permisoRepository;
    private final EmpresaExternaRepository empresaExternaRepository;
    private final ActividadMapper actividadMapper;

    @Transactional(readOnly = true)
    public List<ActividadResponseDTO> listarTodos() {
        return actividadRepository.findAll()
                .stream()
                .map(actividadMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ActividadResponseDTO> listarActivos() {
        return actividadRepository.findByActivoTrue()
                .stream()
                .map(actividadMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ActividadResponseDTO obtenerPorId(Integer id) {

        Actividad actividad = actividadRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        return actividadMapper.toResponseDTO(actividad);
    }

    @Transactional
    public ActividadResponseDTO crear(ActividadRequestDTO dto) {
        validarFechas(dto.getFechaDesde(), dto.getFechaHasta());

        Actividad actividad = actividadMapper.toEntity(dto);
        actividad.setActivo(true);

        actividad = actividadRepository.save(actividad);

        asignarRelaciones(actividad, dto);

        return actividadMapper.toResponseDTO(actividadRepository.save(actividad));
    }

    @Transactional
    public ActividadResponseDTO actualizar(Integer id, ActividadRequestDTO dto) {

        validarFechas(dto.getFechaDesde(), dto.getFechaHasta());

        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));

        actividad.setNombre(dto.getNombre());
        actividad.setFechaDesde(dto.getFechaDesde());
        actividad.setFechaHasta(dto.getFechaHasta());
        actividad.setHoraInicio(dto.getHoraInicio());
        actividad.setHoraSalida(dto.getHoraSalida());
        actividad.setDescripcion(dto.getDescripcion());
        actividad.setLugar(dto.getLugar());
        asignarRelaciones(actividad, dto);
        return actividadMapper.toResponseDTO(actividadRepository.save(actividad));
    }

    @Transactional
    public ActividadResponseDTO asignarNinios(Integer id, List<Integer> niniosIds) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));

        List<Ninio> ninios = ninioRepository.findAllById(niniosIds);
        if (ninios.size() != niniosIds.size()) {
            throw new BusinessException("Uno o más niños no encontrados");
        }
        for (Ninio ninio : ninios) {
            boolean yaExiste = permisoRepository
                    .existsByActividadIdAndNinioId(actividad.getId(), ninio.getId());
            if (!yaExiste) {
                Permiso permiso = new Permiso();
                permiso.setActividad(actividad);
                permiso.setNinio(ninio);
                permiso.setNinioCedula(ninio.getCedula());
                permiso.setAutorizado(false);
                permiso.setActivo(true);
                permisoRepository.save(permiso);
            }
        }
        actividad.setNinios(ninios);
        return actividadMapper.toResponseDTO(actividadRepository.save(actividad));
    }

    @Transactional(readOnly = true)
    public List<ActividadResponseDTO> listarPorNinio(Integer ninioId) {
        if (!ninioRepository.existsById(ninioId)) {
            throw new ResourceNotFoundException("Niño no encontrado");
        }
        return actividadRepository.findByNiniosId(ninioId)
                .stream().map(actividadMapper::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ActividadResponseDTO> listarProximas() {
        return actividadRepository
                .findByActivoTrueAndFechaDesdeGreaterThanEqual(LocalDate.now())
                .stream().map(actividadMapper::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public Map<String, Object> validarAutorizacion(Integer actividadId) {
        Actividad actividad = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));

        List<Permiso> permisos = actividad.getPermisos();
        List<Permiso> pendientes = permisos.stream()
                .filter(p -> p.getAutorizado() == null || !p.getAutorizado())
                .toList();

        return Map.of(
                "actividadId",   actividadId,
                "totalPermisos",  permisos.size(),
                "autorizados",    permisos.size() - pendientes.size(),
                "pendientes",     pendientes.size(),
                "listasPendientes", pendientes.stream()
                        .map(p -> p.getNinioCedula()).toList(),
                "puedeRealizarse", pendientes.isEmpty()
        );
    }

    private void asignarRelaciones(Actividad actividad, ActividadRequestDTO dto) {
        if (dto.getNiniosIds() != null) {
            actividad.setNinios(ninioRepository.findAllById(dto.getNiniosIds()));
        }
        if (dto.getPermisosIds() != null) {
            List<Permiso> permisos = permisoRepository.findAllById(dto.getPermisosIds());
            permisos.forEach(p -> p.setActividad(actividad));
            actividad.setPermisos(permisos);
        }
        if (dto.getEmpresasExternasIds() != null) {
            List<EmpresaExterna> empresas = empresaExternaRepository.findAllById(dto.getEmpresasExternasIds());
            empresas.forEach(e -> e.setActividad(actividad));
            actividad.setEmpresasExternas(empresas);
        }
    }

    private void validarFechas(LocalDate desde, LocalDate hasta) {
        if (hasta.isBefore(desde)) {
            throw new BusinessException("fechaHasta no puede ser anterior a fechaDesde");
        }
    }

    @Transactional
    public void darDeBaja(Integer id) {
        Actividad actividad = actividadRepository.findById(id).orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        if (!actividad.getActivo()) {
            throw new IllegalStateException("La actividad ya está dada de baja");
        }

        actividad.setActivo(false);
        actividad.setFechaBaja(LocalDateTime.now());

        actividadRepository.save(actividad);
    }

    @Transactional
    public ActividadResponseDTO asignarEmpresas(Integer id, List<Integer> empresasIds) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));

        List<EmpresaExterna> empresas = empresaExternaRepository.findAllById(empresasIds);
        if (empresas.size() != empresasIds.size()) {
            throw new BusinessException("Una o más empresas externas no encontradas");
        }
        empresas.forEach(e -> e.setActividad(actividad));
        actividad.setEmpresasExternas(empresas);

        return actividadMapper.toResponseDTO(actividadRepository.save(actividad));
    }

    @Transactional
    public ActividadResponseDTO asignarPermisos(Integer id, List<Integer> permisosIds) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));

        List<Permiso> permisos = permisoRepository.findAllById(permisosIds);
        if (permisos.size() != permisosIds.size()) {
            throw new BusinessException("Uno o más permisos no encontrados");
        }
        permisos.forEach(p -> p.setActividad(actividad));
        actividad.setPermisos(permisos);

        return actividadMapper.toResponseDTO(actividadRepository.save(actividad));
    }

    public void validarPermisoParaActividad(Integer actividadId, Integer ninioId) {
        Actividad actividad = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));

        Permiso permiso = permisoRepository
                .findByActividadIdAndNinioId(actividadId, ninioId)
                .orElseThrow(() -> new BusinessException(
                        "El niño no tiene permiso registrado para esta actividad"));

        if (!permiso.getActivo()) {
            throw new BusinessException("El permiso está dado de baja");
        }
        if (!Boolean.TRUE.equals(permiso.getAutorizado())) {
            throw new BusinessException("El niño no tiene autorización para participar en esta actividad");
        }
    }

    public void validarPermisoParaActividadDelDia(
            Integer actividadId, Integer ninioId, LocalDate fecha) {

        if (actividadId != null) {
            validarPermisoParaActividad(actividadId, ninioId);
            return;
        }

        List<Actividad> actividadesDelDia = actividadRepository
                .findByNiniosIdAndFechaDesde(ninioId, fecha);

        if (actividadesDelDia.isEmpty()) return;

        boolean tienePermisoAutorizado = actividadesDelDia.stream()
                .anyMatch(a -> permisoRepository
                        .findByActividadIdAndNinioId(a.getId(), ninioId)
                        .map(p -> p.getActivo() && Boolean.TRUE.equals(p.getAutorizado()))
                        .orElse(false));

        if (!tienePermisoAutorizado) {
            throw new BusinessException(
                    "El niño tiene actividades programadas para hoy pero no cuenta con permiso autorizado.");
        }
    }

    @Transactional
    public void eliminar(Integer id) {
        Actividad actividad = actividadRepository.findById(id).orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        actividadRepository.delete(actividad);
    }
}
