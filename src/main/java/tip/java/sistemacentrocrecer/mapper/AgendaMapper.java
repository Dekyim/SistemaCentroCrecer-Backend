package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.Agenda;
import tip.java.sistemacentrocrecer.dto.AgendaRequestDTO;
import tip.java.sistemacentrocrecer.dto.AgendaResponseDTO;

@Mapper(componentModel = "spring")
public interface AgendaMapper {

    @Mapping(source = "funcionario.id", target = "funcionarioId")
    @Mapping(source = "funcionario.nombre", target = "funcionarioNombre")
    @Mapping(source = "tipo.id", target = "tipoId")
    @Mapping(source = "tipo.tipo", target = "tipoNombre")
    AgendaResponseDTO toResponseDTO(Agenda agenda);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "funcionario", ignore = true)
    @Mapping(target = "tipo", ignore = true)
    @Mapping(target = "detalles", ignore = true)
    @Mapping(target = "agendasLimpieza", ignore = true)

    Agenda toEntity(AgendaRequestDTO dto);
}