package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.Turno;
import tip.java.sistemacentrocrecer.dto.TurnoRequestDTO;
import tip.java.sistemacentrocrecer.dto.TurnoResponseDTO;

@Mapper(componentModel = "spring")
public interface TurnoMapper {

    @Mapping(source = "funcionario.id", target = "funcionarioId")
    TurnoResponseDTO toResponseDTO(Turno turno);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "funcionario", ignore = true)

    Turno toEntity(TurnoRequestDTO dto);
}