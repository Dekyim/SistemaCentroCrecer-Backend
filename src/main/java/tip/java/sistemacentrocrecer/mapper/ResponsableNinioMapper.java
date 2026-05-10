package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.ResponsableNinio;
import tip.java.sistemacentrocrecer.dto.ResponsableNinioRequestDTO;
import tip.java.sistemacentrocrecer.dto.ResponsableNinioResponseDTO;

@Mapper(componentModel = "spring", uses = {NinioMapper.class, ResponsableMapper.class})
public interface ResponsableNinioMapper {
    // ENTITY -> RESPONSE DTO
    ResponsableNinioResponseDTO toResponseDTO(ResponsableNinio responsableNinio);

    // REQUEST DTO -> ENTITY
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ninio", ignore = true)
    @Mapping(target = "responsable", ignore = true)
    ResponsableNinio toEntity(
            ResponsableNinioRequestDTO dto
    );
}
