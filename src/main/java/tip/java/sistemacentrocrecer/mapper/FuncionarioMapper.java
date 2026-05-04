package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.Funcionario;
import tip.java.sistemacentrocrecer.dto.FuncionarioResponseDTO;
import tip.java.sistemacentrocrecer.dto.FuncionarioRequestDTO;

@Mapper(componentModel = "spring")
public interface FuncionarioMapper {

    @Mapping(target = "grupos", ignore = true)
    FuncionarioResponseDTO toResponseDTO(Funcionario funcionario);

    Funcionario toEntity(FuncionarioRequestDTO dto);
}
