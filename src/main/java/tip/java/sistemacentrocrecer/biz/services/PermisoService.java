package tip.java.sistemacentrocrecer.biz.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tip.java.sistemacentrocrecer.biz.dao.entities.Actividad;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;
import tip.java.sistemacentrocrecer.biz.dao.entities.Permiso;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ActividadRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.NinioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.PermisoRepository;
import tip.java.sistemacentrocrecer.dto.PermisoRequestDTO;
import tip.java.sistemacentrocrecer.dto.PermisoResponseDTO;
import tip.java.sistemacentrocrecer.exceptions.BusinessException;
import tip.java.sistemacentrocrecer.exceptions.ResourceNotFoundException;
import tip.java.sistemacentrocrecer.mapper.PermisoMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class PermisoService {
    private final PermisoRepository permisoRepository;
    private final ActividadRepository actividadRepository;
    private final NinioRepository ninioRepository;
    private final PermisoMapper permisoMapper;

    @Transactional(readOnly = true)
    public List<PermisoResponseDTO> listarTodos() {
        return permisoRepository.findAll()
                .stream()
                .map(permisoMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PermisoResponseDTO> listarActivos() {
        return permisoRepository.findByActivoTrue()
                .stream()
                .map(permisoMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public PermisoResponseDTO obtenerPorId(Integer id) {

        Permiso permiso = permisoRepository.findById(id).orElseThrow(() -> new RuntimeException("Permiso no encontrado"));

        return permisoMapper.toResponseDTO(permiso);
    }

    @Transactional
    public PermisoResponseDTO crear(PermisoRequestDTO dto) {
        if (permisoRepository.existsByActividadIdAndNinioId(dto.getActividadId(), ninioRepository.findByCedula(dto.getNinioCedula())
                .orElseThrow(() -> new ResourceNotFoundException("Niño no encontrado")).getId())) {
            throw new BusinessException("Ya existe un permiso para ese niño en esta actividad");
        }

        Permiso permiso = permisoMapper.toEntity(dto);
        permiso.setActivo(true);

        Actividad actividad = actividadRepository.findById(dto.getActividadId())
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));
        Ninio ninio = ninioRepository.findByCedula(dto.getNinioCedula())
                .orElseThrow(() -> new ResourceNotFoundException("Niño no encontrado"));

        permiso.setActividad(actividad);
        permiso.setNinio(ninio);
        permiso.setNinioCedula(ninio.getCedula());

        return permisoMapper.toResponseDTO(permisoRepository.save(permiso));
    }

    @Transactional
    public PermisoResponseDTO actualizar(Integer id, PermisoRequestDTO dto) {

        Permiso permiso = permisoRepository.findById(id).orElseThrow(() -> new RuntimeException("Permiso no encontrado"));

        Actividad actividad = actividadRepository.findById(dto.getActividadId()).orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        Ninio ninio = ninioRepository.findByCedula(dto.getNinioCedula()).orElseThrow(() -> new RuntimeException("Niño no encontrado"));

        permiso.setActividad(actividad);
        permiso.setNinio(ninio);
        permiso.setNinioCedula(ninio.getCedula());
        permiso.setAutorizado(dto.getAutorizado());

        permiso = permisoRepository.save(permiso);

        return permisoMapper.toResponseDTO(permiso);
    }

    @Transactional
    public void darDeBaja(Integer id) {
        Permiso permiso = permisoRepository.findById(id).orElseThrow(() -> new RuntimeException("Permiso no encontrado"));

        if (!permiso.getActivo()) {
            throw new IllegalStateException("El permiso ya está dado de baja");
        }

        permiso.setActivo(false);
        permiso.setFechaBaja(LocalDateTime.now());
        permisoRepository.save(permiso);
    }

    @Transactional
    public void eliminar(Integer id) {
        Permiso permiso = permisoRepository.findById(id).orElseThrow(() -> new RuntimeException("Permiso no encontrado"));
        permisoRepository.delete(permiso);
    }

    @Transactional(readOnly = true)
    public List<PermisoResponseDTO> listarPorActividad(Integer actividadId) {
        if (!actividadRepository.existsById(actividadId)) {
            throw new ResourceNotFoundException("Actividad no encontrada");
        }
        return permisoRepository.findByActividadId(actividadId)
                .stream().map(permisoMapper::toResponseDTO).toList();
    }

    @Transactional
    public PermisoResponseDTO autorizar(Integer id) {
        Permiso permiso = permisoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permiso no encontrado"));
        if (!permiso.getActivo()) {
            throw new BusinessException("No se puede autorizar un permiso dado de baja");
        }
        validarPlazoModificacion(permiso);
        permiso.setAutorizado(true);
        permiso.setRespondido(true);
        return permisoMapper.toResponseDTO(permisoRepository.save(permiso));
    }

    private void validarPlazoModificacion(Permiso permiso) {
        Actividad actividad = permiso.getActividad();
        if (actividad == null) return;

        // El evento ya ocurrió: no se puede autorizar/rechazar/modificar bajo ninguna circunstancia,
        // tenga o no configurado un plazo límite de modificación.
        LocalDate fechaEvento = actividad.getFechaHasta() != null
                ? actividad.getFechaHasta()
                : actividad.getFechaDesde();
        if (fechaEvento != null && LocalDate.now().isAfter(fechaEvento)) {
            throw new BusinessException(
                    "No se puede modificar la autorización: la actividad ya finalizó el " + fechaEvento
            );
        }

        if (actividad.getDiasLimiteModificacion() == null) return;
        LocalDate limite = actividad.getFechaDesde().minusDays(actividad.getDiasLimiteModificacion());
        if (LocalDate.now().isAfter(limite)) {
            throw new BusinessException(
                    "No se puede modificar la autorización: el plazo límite fue el " + limite
            );
        }
    }

    @Transactional
    public PermisoResponseDTO rechazar(Integer id) {
        Permiso permiso = permisoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permiso no encontrado"));
        if (!permiso.getActivo()) {
            throw new BusinessException("No se puede rechazar un permiso dado de baja");
        }
        validarPlazoModificacion(permiso);
        permiso.setAutorizado(false);
        permiso.setRespondido(true);
        return permisoMapper.toResponseDTO(permisoRepository.save(permiso));
    }

    @Transactional(readOnly = true)
    public List<PermisoResponseDTO> listarPorResponsable(Integer responsableId) {
        return permisoRepository.findByResponsableIdAndActivoTrue(responsableId)
                .stream()
                .map(permisoMapper::toResponseDTO)
                .toList();
    }
}