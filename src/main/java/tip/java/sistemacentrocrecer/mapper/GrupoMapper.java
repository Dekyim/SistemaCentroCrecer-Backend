package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.Grupo;
import tip.java.sistemacentrocrecer.dto.GrupoRequestDTO;
import tip.java.sistemacentrocrecer.dto.GrupoResponseDTO;

@Mapper(componentModel = "spring")
public interface GrupoMapper {
    @Mapping(target = "cantidadNinios", expression = "java(grupo.getNinios() != null ? grupo.getNinios().size() : 0)")
    GrupoResponseDTO toResponseDTO(Grupo grupo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "ninios", ignore = true)
    @Mapping(target = "funcionarios", ignore = true)
    @Mapping(target = "reportes", ignore = true)
    Grupo toEntity(GrupoRequestDTO dto);
}
