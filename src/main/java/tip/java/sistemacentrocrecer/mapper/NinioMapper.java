package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import tip.java.sistemacentrocrecer.biz.dao.entities.Grupo;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;
import tip.java.sistemacentrocrecer.dto.NinioRequestDTO;
import tip.java.sistemacentrocrecer.dto.NinioResponseDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface NinioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "condiciones", ignore = true)
    @Mapping(target = "reportes", ignore = true)
    @Mapping(target = "asistencias", ignore = true)
    @Mapping(target = "responsables", ignore = true)
    @Mapping(target = "permisos", ignore = true)
    @Mapping(target = "inscripciones", ignore = true)
    @Mapping(target = "cedula", source = "dto.cedula")
    @Mapping(target = "nombre", source = "dto.nombre")
    @Mapping(target = "apellido", source = "dto.apellido")
    @Mapping(target = "sexo", source = "dto.sexo")
    @Mapping(target = "direccion", source = "dto.direccion")
    @Mapping(target = "observaciones", source = "dto.observaciones")
    @Mapping(target = "grupo", source = "grupo")
    @Mapping(target = "fechaNacimiento", source = "dto.fechaNacimiento", qualifiedByName = "localDateToDate")
    Ninio toEntity(NinioRequestDTO dto, Grupo grupo);

    @Mapping(target = "fechaNacimiento", source = "fechaNacimiento", qualifiedByName = "dateToLocalDate")
    @Mapping(target = "fechaBaja", source = "fechaBaja", qualifiedByName = "localDateTimeToLocalDate")
    @Mapping(target = "grupoId",     source = "grupo.id")
    @Mapping(target = "grupoNombre", source = "grupo.nombre")
    NinioResponseDTO toDTO(Ninio entity);

    @Named("localDateToDate")
    static Date localDateToDate(LocalDate localDate) {
        if (localDate == null) return null;
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Named("dateToLocalDate")
    static LocalDate dateToLocalDate(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @Named("localDateTimeToLocalDate")
    static LocalDate localDateTimeToLocalDate(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return localDateTime.toLocalDate();
    }
}