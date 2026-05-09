package tip.java.sistemacentrocrecer.biz.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.entities.*;
import tip.java.sistemacentrocrecer.biz.dao.repositories.DocumentoAdjuntoRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ReporteRepository;
import tip.java.sistemacentrocrecer.dto.DocumentoAdjuntoRequestDTO;
import tip.java.sistemacentrocrecer.dto.DocumentoAdjuntoResponseDTO;
import tip.java.sistemacentrocrecer.mapper.DocumentoAdjuntoMapper;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class DocumentoAdjuntoService {

    private final DocumentoAdjuntoRepository documentoAdjuntoRepository;
    private final ReporteRepository reporteRepository;
    private final DocumentoAdjuntoMapper documentoAdjuntoMapper;

    public List<DocumentoAdjuntoResponseDTO> listarTodos() {
        return documentoAdjuntoRepository.findAll()
                .stream()
                .map(documentoAdjuntoMapper::toResponseDTO)
                .toList();
    }

    public DocumentoAdjuntoResponseDTO obtenerPorId(Integer id) {
        DocumentoAdjunto documentoAdjunto = documentoAdjuntoRepository.findById(id).orElseThrow(() -> new RuntimeException("Documento no encontrado con id: " + id));
        return documentoAdjuntoMapper.toResponseDTO(documentoAdjunto);
    }

    @Transactional
    public DocumentoAdjuntoResponseDTO crear(DocumentoAdjuntoRequestDTO dto) {

        DocumentoAdjunto documentoAdjunto = documentoAdjuntoMapper.toEntity(dto);

        Reporte reporte = reporteRepository.findById(dto.getReporteId()).orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + dto.getReporteId()));

        documentoAdjunto.setReporte(reporte);
        documentoAdjunto.setFechaSubida(new Date());

        return documentoAdjuntoMapper.toResponseDTO(documentoAdjuntoRepository.save(documentoAdjunto));
    }

    @Transactional
    public DocumentoAdjuntoResponseDTO actualizar(Integer id, DocumentoAdjuntoRequestDTO dto) {

        DocumentoAdjunto documentoAdjunto = documentoAdjuntoRepository.findById(id).orElseThrow(() -> new RuntimeException("Documento no encontrado con id: " + id));

        documentoAdjunto.setNombreArchivo(dto.getNombreArchivo());
        documentoAdjunto.setTipoArchivo(dto.getTipoArchivo());
        documentoAdjunto.setUrl(dto.getUrl());
        documentoAdjunto.setFechaSubida(new Date());

        Reporte reporte = reporteRepository.findById(dto.getReporteId()).orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + dto.getReporteId()));
        documentoAdjunto.setReporte(reporte);


        return documentoAdjuntoMapper.toResponseDTO(documentoAdjuntoRepository.save(documentoAdjunto));
    }

}