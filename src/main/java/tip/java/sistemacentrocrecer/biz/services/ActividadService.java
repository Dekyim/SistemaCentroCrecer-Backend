package tip.java.sistemacentrocrecer.biz.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
import tip.java.sistemacentrocrecer.mapper.ActividadMapper;

import java.time.LocalDateTime;
import java.util.List;

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

        Actividad actividad = actividadMapper.toEntity(dto);
        actividad.setActivo(true);
        if (dto.getNiniosIds() != null) {
            List<Ninio> ninios = ninioRepository.findAllById(dto.getNiniosIds());
            actividad.setNinios(ninios);
        }

        Actividad finalActividad = actividad;
        if (dto.getPermisosIds() != null) {
            List<Permiso> permisos = permisoRepository.findAllById(dto.getPermisosIds());
            permisos.forEach(permiso -> permiso.setActividad(finalActividad));
            actividad.setPermisos(permisos);
        }

        if (dto.getEmpresasExternasIds() != null) {
            List<EmpresaExterna> empresasExternas = empresaExternaRepository.findAllById(dto.getEmpresasExternasIds());
            empresasExternas.forEach(empresa -> empresa.setActividad(finalActividad));
            actividad.setEmpresasExternas(empresasExternas);
        }

        actividad = actividadRepository.save(actividad);

        return actividadMapper.toResponseDTO(actividad);
    }

    @Transactional
    public ActividadResponseDTO actualizar(Integer id, ActividadRequestDTO dto) {

        Actividad actividad = actividadRepository.findById(id).orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        actividad.setNombre(dto.getNombre());
        actividad.setFechaDesde(dto.getFechaDesde());
        actividad.setFechaHasta(dto.getFechaHasta());
        actividad.setHoraInicio(dto.getHoraInicio());
        actividad.setHoraSalida(dto.getHoraSalida());
        actividad.setDescripcion(dto.getDescripcion());
        actividad.setLugar(dto.getLugar());

        if (dto.getNiniosIds() != null) {
            List<Ninio> ninios = ninioRepository.findAllById(dto.getNiniosIds());
            actividad.setNinios(ninios);
        }

        Actividad finalActividad = actividad;

        if (dto.getPermisosIds() != null) {
            List<Permiso> permisos = permisoRepository.findAllById(dto.getPermisosIds());

            permisos.forEach(permiso -> permiso.setActividad(finalActividad));
            actividad.setPermisos(permisos);
        }

        if (dto.getEmpresasExternasIds() != null) {
            List<EmpresaExterna> empresasExternas = empresaExternaRepository.findAllById(dto.getEmpresasExternasIds());
            empresasExternas.forEach(empresa -> empresa.setActividad(finalActividad));
            actividad.setEmpresasExternas(empresasExternas);
        }

        actividad = actividadRepository.save(actividad);
        return actividadMapper.toResponseDTO(actividad);
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
