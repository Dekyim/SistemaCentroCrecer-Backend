package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.CondicionMedica;
import tip.java.sistemacentrocrecer.dto.CondicionMedicaRequestDTO;
import tip.java.sistemacentrocrecer.dto.CondicionMedicaResponseDTO;

@Mapper(componentModel = "spring")
public interface CondicionMedicaMapper {
    @Mapping(source = "observaciones", target = "observacion")
    CondicionMedicaResponseDTO toResponseDTO(
            CondicionMedica condicionMedica
    );

    @Mapping(target = "condicionId", ignore = true)
    @Mapping(source = "observacion", target = "observaciones")
    @Mapping(target = "ninio", ignore = true)
    CondicionMedica toEntity(
            CondicionMedicaRequestDTO dto
    );
}
