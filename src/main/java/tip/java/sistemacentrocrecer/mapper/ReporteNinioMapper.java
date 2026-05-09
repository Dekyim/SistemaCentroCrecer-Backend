package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.ReporteNinio;
import tip.java.sistemacentrocrecer.dto.ReporteNinioRequestDTO;
import tip.java.sistemacentrocrecer.dto.ReporteNinioResponseDTO;

@Mapper(componentModel = "spring")
public interface ReporteNinioMapper {

    // ENTITY -> RESPONSE DTO
    @Mapping(source = "reporte.id", target = "reporteId")
    @Mapping(source = "reporte.titulo", target = "reporteTitulo")
    @Mapping(source = "ninio.id", target = "ninioId")
    @Mapping(source = "ninio.nombre", target = "ninioNombre")
    ReporteNinioResponseDTO toResponseDTO(ReporteNinio reporteNinio);

    // REQUEST DTO -> ENTITY
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reporte", ignore = true)
    @Mapping(target = "ninio", ignore = true)
    @Mapping(target = "reporteTitulo", ignore = true)
    @Mapping(target = "nombreNinio", ignore = true)
    ReporteNinio toEntity(ReporteNinioRequestDTO dto);
}
