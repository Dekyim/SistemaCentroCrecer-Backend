package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import tip.java.sistemacentrocrecer.biz.dao.entities.Reporte;
import tip.java.sistemacentrocrecer.biz.dao.entities.ReporteGrupo;
import tip.java.sistemacentrocrecer.biz.dao.entities.ReporteNinio;
import tip.java.sistemacentrocrecer.dto.ReporteGrupoResponseDTO;
import tip.java.sistemacentrocrecer.dto.ReporteNinioResponseDTO;
import tip.java.sistemacentrocrecer.dto.ReporteRequestDTO;
import tip.java.sistemacentrocrecer.dto.ReporteResponseDTO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {FuncionarioMapper.class, DocumentoAdjuntoMapper.class})
public interface ReporteMapper {

    @Mapping(source = "reporteGrupos", target = "grupos", qualifiedByName = "gruposToDTO")
    @Mapping(source = "reporteNinios", target = "ninios", qualifiedByName = "niniosToDTO")
    ReporteResponseDTO toResponseDTO(Reporte reporte);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaGeneracion", ignore = true)
    @Mapping(target = "visto", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "funcionario", ignore = true)
    @Mapping(target = "reporteGrupos", ignore = true)
    @Mapping(target = "reporteNinios", ignore = true)
    Reporte toEntity(ReporteRequestDTO dto);

    @Named("gruposToDTO")
    default List<ReporteGrupoResponseDTO> gruposToDTO(List<ReporteGrupo> reporteGrupos) {
        if (reporteGrupos == null) return Collections.emptyList();
        return reporteGrupos.stream().map(rg -> {
            ReporteGrupoResponseDTO dto = new ReporteGrupoResponseDTO();
            dto.setId(rg.getId());
            dto.setReporteId(rg.getReporte() != null ? rg.getReporte().getId() : null);
            dto.setReporteTitulo(rg.getReporte() != null ? rg.getReporte().getTitulo() : null);
            dto.setGrupoId(rg.getGrupo() != null ? rg.getGrupo().getId() : null);
            dto.setGrupoNombre(rg.getGrupo() != null ? rg.getGrupo().getNombre() : null);
            return dto;
        }).collect(Collectors.toList());
    }

    @Named("niniosToDTO")
    default List<ReporteNinioResponseDTO> niniosToDTO(List<ReporteNinio> reporteNinios) {
        if (reporteNinios == null) return Collections.emptyList();
        return reporteNinios.stream().map(rn -> {
            ReporteNinioResponseDTO dto = new ReporteNinioResponseDTO();
            dto.setId(rn.getId());
            dto.setReporteId(rn.getReporte() != null ? rn.getReporte().getId() : null);
            dto.setReporteTitulo(rn.getReporte() != null ? rn.getReporte().getTitulo() : null);
            dto.setNinioId(rn.getNinio() != null ? rn.getNinio().getId() : null);
            dto.setNinioNombre(rn.getNinio() != null ? rn.getNinio().getNombre() : null);
            dto.setNinioApellido(rn.getNinio() != null ? rn.getNinio().getApellido() : null);
            return dto;
        }).collect(Collectors.toList());
    }
}