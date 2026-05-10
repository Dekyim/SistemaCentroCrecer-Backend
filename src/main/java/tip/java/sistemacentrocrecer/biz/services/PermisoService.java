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
import tip.java.sistemacentrocrecer.mapper.PermisoMapper;

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
        Permiso permiso = permisoMapper.toEntity(dto);

        permiso.setActivo(true);

        Actividad actividad = actividadRepository.findById(dto.getActividadId()).orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        Ninio ninio = ninioRepository.findByCedula(dto.getNinioCedula()).orElseThrow(() -> new RuntimeException("Niño no encontrado"));

        permiso.setActividad(actividad);
        permiso.setNinio(ninio);
        permiso.setNinioCedula(ninio.getCedula());

        permiso = permisoRepository.save(permiso);

        return permisoMapper.toResponseDTO(permiso);
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
}
