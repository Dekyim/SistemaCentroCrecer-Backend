package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.EmpresaExterna;
import tip.java.sistemacentrocrecer.dto.EmpresaExternaRequestDTO;
import tip.java.sistemacentrocrecer.dto.EmpresaExternaResponseDTO;

@Mapper(componentModel = "spring")
public interface EmpresaExternaMapper {
    EmpresaExternaResponseDTO toResponseDTO(EmpresaExterna empresaExterna);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actividad", ignore = true)
    EmpresaExterna toEntity(EmpresaExternaRequestDTO dto);
}
