package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import tip.java.sistemacentrocrecer.biz.dao.entities.CondicionMedica;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;
import tip.java.sistemacentrocrecer.biz.dao.enums.SexoNinioEnum;
import tip.java.sistemacentrocrecer.dto.RegistroCompletoRequestDTO;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RegistroCompletoMapper {

    @Mapping(target = "id",           ignore = true)
    @Mapping(target = "activo",       constant = "false")
    @Mapping(target = "fechaBaja",    ignore = true)
    @Mapping(target = "grupo",        ignore = true)
    @Mapping(target = "reportes",     ignore = true)
    @Mapping(target = "asistencias",  ignore = true)
    @Mapping(target = "responsables", ignore = true)
    @Mapping(target = "permisos",     ignore = true)
    @Mapping(target = "inscripciones",ignore = true)
    @Mapping(target = "condiciones",  ignore = true)
    @Mapping(target = "sexo",         source = "sexo",            qualifiedByName = "sexoStringToEnum")
    @Mapping(target = "fechaNacimiento", source = "fechaNacimiento", qualifiedByName = "localDateToDate")
    Ninio toNinio(RegistroCompletoRequestDTO.NinioSolicitudDTO dto);

    default List<CondicionMedica> toCondiciones(
            List<RegistroCompletoRequestDTO.CondicionMedicaSolicitudDTO> dtos,
            Ninio ninio) {
        if (dtos == null || dtos.isEmpty()) return Collections.emptyList();
        List<CondicionMedica> result = new ArrayList<>();
        for (RegistroCompletoRequestDTO.CondicionMedicaSolicitudDTO dto : dtos) {
            result.add(CondicionMedica.builder()
                    .condicion(dto.getCondicion())
                    .observaciones(dto.getObservacion())
                    .esCronica(dto.isEsCronica())
                    .ninio(ninio)
                    .build());
        }
        return result;
    }

    @Named("sexoStringToEnum")
    default SexoNinioEnum sexoStringToEnum(String sexo) {
        if (sexo == null) return null;
        return SexoNinioEnum.valueOf(sexo.toUpperCase());
    }

    @Named("localDateToDate")
    default Date localDateToDate(LocalDate localDate) {
        if (localDate == null) return null;
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}