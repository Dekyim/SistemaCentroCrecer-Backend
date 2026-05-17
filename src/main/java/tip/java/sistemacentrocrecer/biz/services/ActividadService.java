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
    public void eliminar(Integer id) {
        Actividad actividad = actividadRepository.findById(id).orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        actividadRepository.delete(actividad);
    }
}
