package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.Asistencia;
import tip.java.sistemacentrocrecer.dto.AsistenciaResponseDTO;
import tip.java.sistemacentrocrecer.dto.AsistenciaRequestDTO;

@Mapper(componentModel = "spring")
public interface AsistenciaMapper {
    @Mapping(source = "ninio.id", target = "ninioId")
    @Mapping(source = "funcionario.id", target = "funcionarioId")
    AsistenciaRequestDTO toResponseDTO(Asistencia asistencia);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "ninio", ignore = true)
    @Mapping(target = "funcionario", ignore = true)
    Asistencia toEntity(AsistenciaResponseDTO dto);

}
