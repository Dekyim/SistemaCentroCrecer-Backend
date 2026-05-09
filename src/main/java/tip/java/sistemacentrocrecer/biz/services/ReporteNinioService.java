package tip.java.sistemacentrocrecer.biz.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;
import tip.java.sistemacentrocrecer.biz.dao.entities.Reporte;
import tip.java.sistemacentrocrecer.biz.dao.entities.ReporteNinio;
import tip.java.sistemacentrocrecer.biz.dao.repositories.NinioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ReporteNinioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ReporteRepository;
import tip.java.sistemacentrocrecer.dto.ReporteNinioRequestDTO;
import tip.java.sistemacentrocrecer.dto.ReporteNinioResponseDTO;
import tip.java.sistemacentrocrecer.mapper.ReporteNinioMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteNinioService {
    private final ReporteNinioRepository reporteNinioRepository;
    private final ReporteRepository reporteRepository;
    private final NinioRepository ninioRepository;
    private final ReporteNinioMapper reporteNinioMapper;

    @Transactional(readOnly = true)
    public List<ReporteNinioResponseDTO> listarTodos() {
        return reporteNinioRepository.findAll()
                .stream()
                .map(reporteNinioMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReporteNinioResponseDTO obtenerPorId(Integer id) {
        ReporteNinio reporteNinio = reporteNinioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReporteNinio no encontrado"));

        return reporteNinioMapper.toResponseDTO(reporteNinio);
    }

    @Transactional
    public ReporteNinioResponseDTO crear(ReporteNinioRequestDTO dto) {
        ReporteNinio reporteNinio = reporteNinioMapper.toEntity(dto);

        Reporte reporte = reporteRepository.findById(dto.getReporteId())
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        Ninio ninio = ninioRepository.findById(dto.getNinioId())
                .orElseThrow(() -> new RuntimeException("Niño no encontrado"));

        reporteNinio.setReporte(reporte);
        reporteNinio.setNinio(ninio);

        reporteNinio.setReporteTitulo(reporte.getTitulo());
        reporteNinio.setNombreNinio(ninio.getNombre());
        reporteNinio = reporteNinioRepository.save(reporteNinio);

        return reporteNinioMapper.toResponseDTO(reporteNinio);
    }

    @Transactional
    public ReporteNinioResponseDTO actualizar(Integer id, ReporteNinioRequestDTO dto) {
        ReporteNinio reporteNinio = reporteNinioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReporteNinio no encontrado"));

        Reporte reporte = reporteRepository.findById(dto.getReporteId())
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        Ninio ninio = ninioRepository.findById(dto.getNinioId())
                .orElseThrow(() -> new RuntimeException("Niño no encontrado"));

        reporteNinio.setReporte(reporte);
        reporteNinio.setNinio(ninio);

        reporteNinio.setReporteTitulo(reporte.getTitulo());
        reporteNinio.setNombreNinio(ninio.getNombre());
        reporteNinio = reporteNinioRepository.save(reporteNinio);

        return reporteNinioMapper.toResponseDTO(reporteNinio);
    }

    @Transactional
    public void eliminar(Integer id) {
        ReporteNinio reporteNinio = reporteNinioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReporteNinio no encontrado"));

        reporteNinioRepository.delete(reporteNinio);
    }

}
