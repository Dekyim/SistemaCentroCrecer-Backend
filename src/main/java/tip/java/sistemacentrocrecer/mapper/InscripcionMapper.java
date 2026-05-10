package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.Inscripcion;
import tip.java.sistemacentrocrecer.dto.InscripcionRequestDTO;
import tip.java.sistemacentrocrecer.dto.InscripcionResponseDTO;

@Mapper(componentModel = "spring", uses = {NinioMapper.class, ResponsableMapper.class})
public interface InscripcionMapper {
    @Mapping(source = "responsables", target = "responsables")
    InscripcionResponseDTO toResponseDTO(Inscripcion inscripcion);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "motivoBaja", ignore = true)
    @Mapping(target = "ninio", ignore = true)
    @Mapping(target = "responsables", ignore = true)
    Inscripcion toEntity(InscripcionRequestDTO dto);
}
