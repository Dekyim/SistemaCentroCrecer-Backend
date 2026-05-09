package tip.java.sistemacentrocrecer.biz.services;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.entities.*;
import tip.java.sistemacentrocrecer.biz.dao.repositories.FuncionarioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.GrupoRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.NinioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ReporteRepository;
import tip.java.sistemacentrocrecer.dto.ReporteRequestDTO;
import tip.java.sistemacentrocrecer.dto.ReporteResponseDTO;
import tip.java.sistemacentrocrecer.mapper.DocumentoAdjuntoMapper;
import tip.java.sistemacentrocrecer.mapper.ReporteMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ReporteService {
    private final ReporteRepository reporteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final GrupoRepository grupoRepository;
    private final NinioRepository ninioRepository;

    private final ReporteMapper reporteMapper;
    private final DocumentoAdjuntoMapper documentoAdjuntoMapper;

    @Transactional
    public ReporteResponseDTO crearReporte(ReporteRequestDTO dto) {
        Reporte reporte = reporteMapper.toEntity(dto);
        reporte.setFechaGeneracion(new Date());
        reporte.setActivo(true);
        reporte.setVisto(true);

        // Funcionario
        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new RuntimeException("Funcionario no encontrado"));
        reporte.setFuncionario(funcionario);

        // Primero guardar el reporte
        reporte = reporteRepository.save(reporte);

        // Grupos
        List<ReporteGrupo> reporteGrupos = new ArrayList<>();

        if (dto.getGruposIds() != null) {
            for (Integer grupoId : dto.getGruposIds()) {
                Grupo grupo = grupoRepository.findById(grupoId)
                        .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
                ReporteGrupo rg = new ReporteGrupo();
                rg.setReporte(reporte);
                rg.setGrupo(grupo);

                reporteGrupos.add(rg);

            }
        }
        reporte.setReporteGrupos(reporteGrupos);

        // Niños
        List<ReporteNinio> reporteNinios = new ArrayList<>();
        if (dto.getNiniosIds() != null) {
            for (Integer ninioId : dto.getNiniosIds()) {
                Ninio ninio = ninioRepository.findById(ninioId)
                        .orElseThrow(() -> new RuntimeException("Ninio no encontrado"));

                ReporteNinio rn = new ReporteNinio();
                rn.setReporte(reporte);
                rn.setNinio(ninio);

                reporteNinios.add(rn);
            }
        }
        reporte.setReporteNinios(reporteNinios);

        // Documentos
        if (dto.getDocumentos() != null) {

            List<DocumentoAdjunto> documentos = dto.getDocumentos()
                    .stream()
                    .map(documentoAdjuntoMapper::toEntity)
                    .collect(java.util.stream.Collectors.toList());

            for (DocumentoAdjunto doc : documentos) {
                doc.setReporte(reporte);
            }

            reporte.setDocumentos(documentos);
        }
        reporte = reporteRepository.save(reporte);

        return reporteMapper.toResponseDTO(reporte);

    }

    public List<ReporteResponseDTO> listarTodos(){
        return reporteRepository.findAll()
                .stream()
                .map(reporteMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public ReporteResponseDTO actualizarReporte(Integer id, ReporteRequestDTO dto) {
        //Buscar reporte
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        reporte.setTitulo(dto.getTitulo());
        reporte.setDescripcion(dto.getDescripcion());

        // Funcionario
        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new RuntimeException("Funcionario no encontrado"));
        reporte.setFuncionario(funcionario);

        // Grupos
        List<ReporteGrupo> reporteGrupos = new ArrayList<>();

        if (dto.getGruposIds() != null) {

            for (Integer grupoId : dto.getGruposIds()) {

                Grupo grupo = grupoRepository.findById(grupoId)
                        .orElseThrow(() ->
                                new RuntimeException("Grupo no encontrado"));

                ReporteGrupo rg = new ReporteGrupo();
                rg.setReporte(reporte);
                rg.setGrupo(grupo);

                reporteGrupos.add(rg);
            }
        }
        reporte.setReporteGrupos(reporteGrupos);

        //Niños
        List<ReporteNinio> reporteNinios = new ArrayList<>();

        if (dto.getNiniosIds() != null) {

            for (Integer ninioId : dto.getNiniosIds()) {

                Ninio ninio = ninioRepository.findById(ninioId)
                        .orElseThrow(() ->
                                new RuntimeException("Niño no encontrado"));

                ReporteNinio rn = new ReporteNinio();
                rn.setReporte(reporte);
                rn.setNinio(ninio);

                reporteNinios.add(rn);
            }
        }
        reporte.setReporteNinios(reporteNinios);

        // Documentos
        if (dto.getDocumentos() != null) {

            List<DocumentoAdjunto> documentos = dto.getDocumentos()
                    .stream()
                    .map(documentoAdjuntoMapper::toEntity)
                    .collect(java.util.stream.Collectors.toList());

            for (DocumentoAdjunto doc : documentos) {
                doc.setReporte(reporte);
            }

            reporte.setDocumentos(documentos);
        }
        reporte = reporteRepository.save(reporte);
        return reporteMapper.toResponseDTO(reporte);
    }

    @Transactional(readOnly = true)
    public ReporteResponseDTO obtenerPorId(Integer id) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        return reporteMapper.toResponseDTO(reporte);
    }

    @Transactional
    public void darDeBaja(Integer id){
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        reporte.setActivo(false);
        reporte.setFechaBaja(LocalDateTime.now());
        reporteRepository.save(reporte);

    }

    public List<ReporteResponseDTO> listarActivos(){
        return reporteRepository.findByActivoTrue().stream()
                .map(reporteMapper::toResponseDTO)
                .toList();
    }
}
