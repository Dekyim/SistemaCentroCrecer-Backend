package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.AgendaLimpieza;
import tip.java.sistemacentrocrecer.dto.AgendaLimpiezaRequestDTO;
import tip.java.sistemacentrocrecer.dto.AgendaLimpiezaResponseDTO;

@Mapper(componentModel = "spring")
public interface AgendaLimpiezaMapper {

    @Mapping(source = "agenda.id", target = "agendaId")
    @Mapping(source = "subtipoAgenda.subtipoId", target = "subtipoAgendaId")
    @Mapping(source = "subtipoAgenda.subtipo", target = "subtipoAgendaNombre")
    AgendaLimpiezaResponseDTO toResponseDTO(AgendaLimpieza agendaLimpieza);

    @Mapping(target = "agenda", ignore = true)
    @Mapping(target = "subtipoAgenda", ignore = true)
    @Mapping(target = "estado", constant = "PENDIENTE")
    AgendaLimpieza toEntity(AgendaLimpiezaRequestDTO dto);
}