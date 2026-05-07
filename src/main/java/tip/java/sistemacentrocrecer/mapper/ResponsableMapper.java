package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tip.java.sistemacentrocrecer.biz.dao.entities.Responsable;
import tip.java.sistemacentrocrecer.dto.ResponsableRequestDTO;
import tip.java.sistemacentrocrecer.dto.ResponsableResponseDTO;

@Mapper(componentModel = "spring")
public interface ResponsableMapper {
    ResponsableResponseDTO toDTO(Responsable responsable);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "fecha_baja", ignore = true)
    Responsable toEntity(ResponsableRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "fecha_baja", ignore = true)
    void updateEntityFromDTO(ResponsableRequestDTO dto, @MappingTarget Responsable responsable);


}
