package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.Actividad;
import tip.java.sistemacentrocrecer.dto.ActividadRequestDTO;
import tip.java.sistemacentrocrecer.dto.ActividadResponseDTO;

@Mapper(componentModel = "spring", uses = {NinioMapper.class, PermisoMapper.class, EmpresaExternaMapper.class})
public interface ActividadMapper {

    ActividadResponseDTO toResponseDTO(
            Actividad actividad
    );

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)

    @Mapping(target = "ninios", ignore = true)
    @Mapping(target = "permisos", ignore = true)
    @Mapping(target = "empresasExternas", ignore = true)

    Actividad toEntity(ActividadRequestDTO dto);
}
