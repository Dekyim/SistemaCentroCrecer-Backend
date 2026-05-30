package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.Funcionario;
import tip.java.sistemacentrocrecer.dto.FuncionarioResponseDTO;
import tip.java.sistemacentrocrecer.dto.FuncionarioRequestDTO;

@Mapper(componentModel = "spring")
public interface FuncionarioMapper {

    FuncionarioResponseDTO toResponseDTO(Funcionario funcionario);

    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "fotoPerfil", ignore = true)
    @Mapping(target = "mustChangePassword", constant = "false")
    @Mapping(target = "reportes", ignore = true)
    @Mapping(target = "grupos", ignore = true)
    @Mapping(target = "turnos", ignore = true)
    @Mapping(target = "asistencias", ignore = true)
    @Mapping(target = "agendas", ignore = true)
    Funcionario toEntity(FuncionarioRequestDTO dto);
}