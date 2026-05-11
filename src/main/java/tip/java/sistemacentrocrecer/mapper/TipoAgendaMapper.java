package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.TipoAgenda;
import tip.java.sistemacentrocrecer.dto.TipoAgendaRequestDTO;
import tip.java.sistemacentrocrecer.dto.TipoAgendaResponseDTO;

@Mapper(componentModel = "spring")
public interface TipoAgendaMapper {

    TipoAgendaResponseDTO toResponseDTO(TipoAgenda tipoAgenda);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "agendas", ignore = true)
    TipoAgenda toEntity(TipoAgendaRequestDTO dto);

}