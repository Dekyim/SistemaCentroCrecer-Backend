package tip.java.sistemacentrocrecer.biz.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tip.java.sistemacentrocrecer.biz.dao.entities.Actividad;
import tip.java.sistemacentrocrecer.biz.dao.entities.EmpresaExterna;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ActividadRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.EmpresaExternaRepository;
import tip.java.sistemacentrocrecer.dto.EmpresaExternaRequestDTO;
import tip.java.sistemacentrocrecer.dto.EmpresaExternaResponseDTO;
import tip.java.sistemacentrocrecer.exceptions.BusinessException;
import tip.java.sistemacentrocrecer.exceptions.ResourceNotFoundException;
import tip.java.sistemacentrocrecer.mapper.EmpresaExternaMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class EmpresaExternaService {
    private final EmpresaExternaRepository empresaExternaRepository;
    private final ActividadRepository actividadRepository;
    private final EmpresaExternaMapper empresaExternaMapper;

    @Transactional(readOnly = true)
    public List<EmpresaExternaResponseDTO> listarTodos() {
        return empresaExternaRepository.findAll()
                .stream()
                .map(empresaExternaMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmpresaExternaResponseDTO obtenerPorId(Integer id) {
        EmpresaExterna empresaExterna = empresaExternaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa externa no encontrada"));

        return empresaExternaMapper.toResponseDTO(empresaExterna);
    }

    @Transactional
    public EmpresaExternaResponseDTO crear(EmpresaExternaRequestDTO dto) {
        if (empresaExternaRepository.existsByNombreAndActividadId(
                dto.getNombre(), dto.getActividadId())) {
            throw new BusinessException(
                    "Ya existe una empresa con ese nombre en esta actividad"
            );
        }

        EmpresaExterna empresa = empresaExternaMapper.toEntity(dto);
        Actividad actividad = actividadRepository.findById(dto.getActividadId())
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));
        empresa.setActividad(actividad);
        return empresaExternaMapper.toResponseDTO(empresaExternaRepository.save(empresa));
    }

    @Transactional
    public EmpresaExternaResponseDTO actualizar(Integer id, EmpresaExternaRequestDTO dto) {
        EmpresaExterna empresaExterna = empresaExternaRepository.findById(id).orElseThrow(() -> new RuntimeException("Empresa externa no encontrada"));

        Actividad actividad = actividadRepository.findById(dto.getActividadId())
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        empresaExterna.setNombre(dto.getNombre());
        empresaExterna.setTipoServicio(dto.getTipoServicio());
        empresaExterna.setTelefono(dto.getTelefono());
        empresaExterna.setActividad(actividad);

        empresaExterna = empresaExternaRepository.save(empresaExterna);

        return empresaExternaMapper.toResponseDTO(empresaExterna);
    }

    @Transactional(readOnly = true)
    public List<EmpresaExternaResponseDTO> listarPorActividad(Integer actividadId) {
        if (!actividadRepository.existsById(actividadId)) {
            throw new ResourceNotFoundException("Actividad no encontrada");
        }
        return empresaExternaRepository.findByActividadId(actividadId)
                .stream().map(empresaExternaMapper::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<EmpresaExternaResponseDTO> listarPorTipoServicio(String tipoServicio) {
        return empresaExternaRepository.findByTipoServicioIgnoreCase(tipoServicio)
                .stream().map(empresaExternaMapper::toResponseDTO).toList();
    }

    @Transactional
    public EmpresaExternaResponseDTO desasignarActividad(Integer id) {
        EmpresaExterna empresa = empresaExternaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa externa no encontrada"));
        empresa.setActividad(null);
        return empresaExternaMapper.toResponseDTO(empresaExternaRepository.save(empresa));
    }

    @Transactional
    public void eliminar(Integer id) {
        EmpresaExterna empresaExterna = empresaExternaRepository.findById(id).orElseThrow(() -> new RuntimeException("Empresa externa no encontrada"));
        empresaExternaRepository.delete(empresaExterna);
    }

}
