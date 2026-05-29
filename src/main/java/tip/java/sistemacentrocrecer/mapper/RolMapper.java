package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.Rol;
import tip.java.sistemacentrocrecer.dto.RolResponseDTO;
import tip.java.sistemacentrocrecer.dto.RolRequestDTO;

@Mapper(componentModel = "spring")
public interface RolMapper {
    @Mapping(source = "padre.id", target = "padreId")
    @Mapping(source = "padre.nombre", target = "padreNombre")
    RolResponseDTO toResponseDTO(Rol rol);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "padre", ignore = true)
    @Mapping(target = "hijos", ignore = true)
    @Mapping(target = "funcionarios", ignore = true)
    Rol toEntity(RolRequestDTO dto);
}