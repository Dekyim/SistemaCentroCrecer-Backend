package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tip.java.sistemacentrocrecer.biz.dao.entities.DocumentoAdjunto;
import tip.java.sistemacentrocrecer.dto.DocumentoAdjuntoRequestDTO;
import tip.java.sistemacentrocrecer.dto.DocumentoAdjuntoResponseDTO;

@Mapper(componentModel = "spring")
public interface DocumentoAdjuntoMapper {

    @Mapping(source = "reporte.id", target = "reporteId")
    @Mapping(source = "reporte.titulo", target = "reporteTitulo")
    DocumentoAdjuntoResponseDTO toResponseDTO(DocumentoAdjunto documentoAdjunto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaSubida", ignore = true)
    @Mapping(target = "reporte", ignore = true)

    DocumentoAdjunto toEntity(DocumentoAdjuntoRequestDTO dto);
}

