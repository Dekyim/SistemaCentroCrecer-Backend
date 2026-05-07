package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.SubtipoAgenda;
import tip.java.sistemacentrocrecer.dto.SubtipoAgendaRequestDTO;
import tip.java.sistemacentrocrecer.dto.SubtipoAgendaResponseDTO;

@Mapper(componentModel = "spring")
public interface SubtipoAgendaMapper {

    @Mapping(source = "detalleAgenda.id", target = "detalleAgendaId")
    @Mapping(source = "agendaLimpieza.nombre", target = "agendaLimpiezaId")
    SubtipoAgendaResponseDTO toResponseDTO(SubtipoAgenda subtipoAgenda);

    @Mapping(target = "subtipoId", ignore = true)
    @Mapping(target = "detalleAgenda", ignore = true)
    @Mapping(target = "agendaLimpieza", ignore = true)
    SubtipoAgenda toEntity(SubtipoAgendaRequestDTO dto);
}