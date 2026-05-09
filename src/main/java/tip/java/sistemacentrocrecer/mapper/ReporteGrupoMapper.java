package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.ReporteGrupo;
import tip.java.sistemacentrocrecer.dto.ReporteGrupoRequestDTO;
import tip.java.sistemacentrocrecer.dto.ReporteGrupoResponseDTO;

@Mapper(componentModel = "spring")
public interface ReporteGrupoMapper {
    // ENTITY -> RESPONSE DTO
    @Mapping(source = "reporte.id", target = "reporteId")
    @Mapping(source = "grupo.nombre", target = "grupoNombre")
    @Mapping(source = "reporte.titulo", target = "reporteTitulo")
    @Mapping(source = "grupo.id", target = "grupoId")
    ReporteGrupoResponseDTO toResponseDTO(ReporteGrupo reporteGrupo);

    // REQUEST DTO -> ENTITY
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reporte", ignore = true)
    @Mapping(target = "grupo", ignore = true)
    @Mapping(target = "reporteTitulo", ignore = true)
    @Mapping(target = "grupoNombre", ignore = true)
    ReporteGrupo toEntity(ReporteGrupoRequestDTO dto);
}
