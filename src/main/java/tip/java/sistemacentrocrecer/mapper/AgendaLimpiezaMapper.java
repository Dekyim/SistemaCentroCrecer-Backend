package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.AgendaLimpieza;
import tip.java.sistemacentrocrecer.dto.AgendaLimpiezaRequestDTO;
import tip.java.sistemacentrocrecer.dto.AgendaLimpiezaResponseDTO;

@Mapper(componentModel = "spring")
public interface AgendaLimpiezaMapper {

    @Mapping(source = "funcionario.id",     target = "funcionarioId")
    @Mapping(source = "funcionario.nombre", target = "funcionarioNombre")
    @Mapping(source = "subtipoAgenda.subtipoId", target = "subtipoAgendaId")
    @Mapping(source = "subtipoAgenda.subtipo",   target = "subtipoAgendaNombre")
    AgendaLimpiezaResponseDTO toResponseDTO(AgendaLimpieza agendaLimpieza);

    @Mapping(target = "funcionario",    ignore = true)
    @Mapping(target = "subtipoAgenda",  ignore = true)
    @Mapping(target = "estado",         constant = "PENDIENTE")
    @Mapping(target = "id",            ignore = true)
    AgendaLimpieza toEntity(AgendaLimpiezaRequestDTO dto);
}
