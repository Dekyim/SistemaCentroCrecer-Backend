package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.Asistencia;
import tip.java.sistemacentrocrecer.dto.AsistenciaResponseDTO;
import tip.java.sistemacentrocrecer.dto.AsistenciaRequestDTO;
import tip.java.sistemacentrocrecer.dto.AsistenciaNinioRequestDTO;

@Mapper(componentModel = "spring")
public interface AsistenciaMapper {

    @Mapping(source = "ninio.id", target = "ninioId")
    @Mapping(source = "ninio.apellido", target = "ninioApellido")
    @Mapping(source = "ninio.grupo.nombre", target = "grupoNombre")
    @Mapping(source = "funcionario.id", target = "funcionarioId")
    AsistenciaResponseDTO toResponseDTO(Asistencia asistencia);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "ninio", ignore = true)
    @Mapping(target = "funcionario", ignore = true)
    @Mapping(target = "ninioNombre", ignore = true)
    @Mapping(target = "ninioCedula", ignore = true)
    @Mapping(target = "funcionarioNombre", ignore = true)
    @Mapping(target = "funcionarioCedula", ignore = true)
    Asistencia toEntity(AsistenciaRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "ninio", ignore = true)
    @Mapping(target = "funcionario", ignore = true)
    @Mapping(target = "ninioNombre", ignore = true)
    @Mapping(target = "ninioCedula", ignore = true)
    @Mapping(target = "funcionarioNombre", ignore = true)
    @Mapping(target = "funcionarioCedula", ignore = true)
    Asistencia toEntityFromNinio(AsistenciaNinioRequestDTO dto);
}