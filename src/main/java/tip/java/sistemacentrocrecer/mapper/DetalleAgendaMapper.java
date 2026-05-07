package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.DetalleAgenda;
import tip.java.sistemacentrocrecer.dto.DetalleAgendaRequestDTO;
import tip.java.sistemacentrocrecer.dto.DetalleAgendaResponseDTO;

@Mapper(componentModel = "spring")
public interface DetalleAgendaMapper {

    @Mapping(source = "agenda.id", target = "agendaId")
    @Mapping(source = "subtipoAgenda.subtipoId", target = "subtipoAgendaId")
    @Mapping(source = "subtipoAgenda.subtipo", target = "subtipoAgendaNombre")
    DetalleAgendaResponseDTO toResponseDTO(DetalleAgenda detalleAgenda);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "agenda", ignore = true)
    @Mapping(target = "subtipoAgenda", ignore = true)

    DetalleAgenda toEntity(DetalleAgendaRequestDTO dto);
}

