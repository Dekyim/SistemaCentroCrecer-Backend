package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.Permiso;
import tip.java.sistemacentrocrecer.dto.PermisoRequestDTO;
import tip.java.sistemacentrocrecer.dto.PermisoResponseDTO;

@Mapper(componentModel = "spring")
public interface PermisoMapper {

    @Mapping(target = "actividad.ninios",   ignore = true)
    PermisoResponseDTO toResponseDTO(Permiso permiso);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "actividad", ignore = true)
    @Mapping(target = "ninio", ignore = true)
    @Mapping(target = "responsables", ignore = true)
    Permiso toEntity(PermisoRequestDTO dto);
}
