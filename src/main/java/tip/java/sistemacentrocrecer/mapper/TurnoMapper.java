package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import tip.java.sistemacentrocrecer.biz.dao.entities.Funcionario;
import tip.java.sistemacentrocrecer.biz.dao.entities.Turno;
import tip.java.sistemacentrocrecer.dto.TurnoRequestDTO;
import tip.java.sistemacentrocrecer.dto.TurnoResponseDTO;

@Mapper(componentModel = "spring")
public interface TurnoMapper {

    @Mapping(source = "funcionario.id", target = "funcionarioId")
    @Mapping(source = "funcionario", target = "funcionarioNombre", qualifiedByName = "nombreCompleto")
    @Mapping(source = "dias", target = "dias")
    TurnoResponseDTO toResponseDTO(Turno turno);

    @Named("nombreCompleto")
    default String nombreCompleto(Funcionario funcionario) {
        if (funcionario == null) return null;
        return funcionario.getNombre() + " " + funcionario.getApellido();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "funcionario", ignore = true)
    @Mapping(source = "dias", target = "dias")
    Turno toEntity(TurnoRequestDTO dto);
}