package tip.java.sistemacentrocrecer.biz.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tip.java.sistemacentrocrecer.biz.dao.entities.Grupo;
import tip.java.sistemacentrocrecer.biz.dao.entities.Reporte;
import tip.java.sistemacentrocrecer.biz.dao.entities.ReporteGrupo;
import tip.java.sistemacentrocrecer.biz.dao.repositories.GrupoRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ReporteGrupoRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ReporteRepository;
import tip.java.sistemacentrocrecer.dto.ReporteGrupoRequestDTO;
import tip.java.sistemacentrocrecer.dto.ReporteGrupoResponseDTO;
import tip.java.sistemacentrocrecer.mapper.ReporteGrupoMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteGrupoService {
    private final ReporteGrupoRepository reporteGrupoRepository;
    private final ReporteRepository reporteRepository;
    private final GrupoRepository grupoRepository;
    private final ReporteGrupoMapper reporteGrupoMapper;

    @Transactional(readOnly = true)
    public List<ReporteGrupoResponseDTO> listarTodos() {
        return reporteGrupoRepository.findAll()
                .stream()
                .map(reporteGrupoMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReporteGrupoResponseDTO obtenerPorId(Integer id) {
        ReporteGrupo reporteGrupo = reporteGrupoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReporteGrupo no encontrado"));

        return reporteGrupoMapper.toResponseDTO(reporteGrupo);
    }

    @Transactional
    public ReporteGrupoResponseDTO crear(ReporteGrupoRequestDTO dto) {
        ReporteGrupo reporteGrupo = reporteGrupoMapper.toEntity(dto);
        Reporte reporte = reporteRepository.findById(dto.getReporteId())
                .orElseThrow(() -> new RuntimeException("ReporteGrupo no encontrado"));

        Grupo grupo = grupoRepository.findById(dto.getGrupoId())
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        reporteGrupo.setReporte(reporte);
        reporteGrupo.setGrupo(grupo);

        reporteGrupo.setReporteTitulo(reporte.getTitulo());
        reporteGrupo.setGrupoNombre(grupo.getNombre());
        reporteGrupo = reporteGrupoRepository.save(reporteGrupo);

        return reporteGrupoMapper.toResponseDTO(reporteGrupo);
    }

    @Transactional
    public ReporteGrupoResponseDTO actualizar(Integer id, ReporteGrupoRequestDTO dto) {
        ReporteGrupo reporteGrupo = reporteGrupoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReporteGrupo no encontrado"));

        Reporte reporte = reporteRepository.findById(dto.getReporteId())
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        Grupo grupo = grupoRepository.findById(dto.getGrupoId())
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        reporteGrupo.setReporte(reporte);
        reporteGrupo.setGrupo(grupo);

        reporteGrupo.setReporteTitulo(reporte.getTitulo());
        reporteGrupo.setGrupoNombre(grupo.getNombre());
        reporteGrupo = reporteGrupoRepository.save(reporteGrupo);

        return reporteGrupoMapper.toResponseDTO(reporteGrupo);
    }

    @Transactional
    public void eliminar(Integer id) {
        ReporteGrupo reporteGrupo = reporteGrupoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReporteGrupo no encontrado"));

        reporteGrupoRepository.delete(reporteGrupo);
    }
}
