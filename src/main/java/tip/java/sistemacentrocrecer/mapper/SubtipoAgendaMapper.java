package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.SubtipoAgenda;
import tip.java.sistemacentrocrecer.dto.SubtipoAgendaRequestDTO;
import tip.java.sistemacentrocrecer.dto.SubtipoAgendaResponseDTO;

@Mapper(componentModel = "spring")
public interface SubtipoAgendaMapper {

    SubtipoAgendaResponseDTO toResponseDTO(SubtipoAgenda subtipoAgenda);

    @Mapping(target = "subtipoId", ignore = true)
    SubtipoAgenda toEntity(SubtipoAgendaRequestDTO dto);
}