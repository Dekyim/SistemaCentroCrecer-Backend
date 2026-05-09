package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import tip.java.sistemacentrocrecer.biz.dao.entities.Reporte;
import tip.java.sistemacentrocrecer.biz.dao.entities.ReporteGrupo;
import tip.java.sistemacentrocrecer.biz.dao.entities.ReporteNinio;
import tip.java.sistemacentrocrecer.dto.ReporteRequestDTO;
import tip.java.sistemacentrocrecer.dto.ReporteResponseDTO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {FuncionarioMapper.class, DocumentoAdjuntoMapper.class})// Para convertir objetos complejos
public interface ReporteMapper {
    //Metodos personalizados para mapear campos complejos que con conversion automatica no funcionaria
    @Mapping(source = "reporteGrupos", target = "grupos", qualifiedByName = "gruposToNombres")
    @Mapping(source = "reporteNinios", target = "ninios", qualifiedByName = "niniosToNombres")
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

    // Metodo de conversion personalizado
    @Named("gruposToNombres")
    default List<String> gruposToNombres(List<ReporteGrupo> reporteGrupos) {
        if (reporteGrupos == null) return Collections.emptyList();
        return reporteGrupos.stream()
                .map(rg -> rg.getGrupo().getNombre())
                .collect(Collectors.toList());
    }

    // Metodo de conversion personalizado
    @Named("niniosToNombres")
    default List<String> niniosToNombres(List<ReporteNinio> reporteNinios) {
        if (reporteNinios == null) return Collections.emptyList();
        return reporteNinios.stream()
                .map(rn -> rn.getNinio().getNombre())
                .collect(Collectors.toList());
    }

}
