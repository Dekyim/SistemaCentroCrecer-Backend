package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import tip.java.sistemacentrocrecer.biz.dao.entities.CondicionMedica;
import tip.java.sistemacentrocrecer.biz.dao.entities.Grupo;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;
import tip.java.sistemacentrocrecer.dto.CondicionMedicaResponseDTO;
import tip.java.sistemacentrocrecer.dto.NinioRequestDTO;
import tip.java.sistemacentrocrecer.dto.NinioResponseDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface NinioMapper {

    @Mapping(target = "id",           ignore = true)
    @Mapping(target = "activo",       constant = "true")
    @Mapping(target = "fechaBaja",    ignore = true)
    @Mapping(target = "condiciones",  ignore = true)
    @Mapping(target = "reportes",     ignore = true)
    @Mapping(target = "asistencias",  ignore = true)
    @Mapping(target = "responsables", ignore = true)
    @Mapping(target = "permisos",     ignore = true)
    @Mapping(target = "inscripciones",ignore = true)
    @Mapping(target = "cedula",           source = "dto.cedula")
    @Mapping(target = "nombre",           source = "dto.nombre")
    @Mapping(target = "apellido",         source = "dto.apellido")
    @Mapping(target = "sexo",             source = "dto.sexo")
    @Mapping(target = "direccion",        source = "dto.direccion")
    @Mapping(target = "observaciones",    source = "dto.observaciones")
    @Mapping(target = "grupo",            source = "grupo")
    @Mapping(target = "fechaNacimiento",  source = "dto.fechaNacimiento", qualifiedByName = "localDateToDate")
    Ninio toEntity(NinioRequestDTO dto, Grupo grupo);

    @Mapping(target = "fechaNacimiento", source = "fechaNacimiento",  qualifiedByName = "dateToLocalDate")
    @Mapping(target = "fechaBaja",       source = "fechaBaja",        qualifiedByName = "localDateTimeToLocalDate")
    @Mapping(target = "grupoId",         source = "grupo.id")
    @Mapping(target = "grupoNombre",     source = "grupo.nombre")
    @Mapping(target = "grupo",           source = "grupo",            qualifiedByName = "grupoToResumen")
    @Mapping(target = "condicionesMedicas", source = "condiciones",   qualifiedByName = "condicionesToDTO")
    NinioResponseDTO toDTO(Ninio entity);

    default List<NinioResponseDTO> toDTOList(List<Ninio> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(this::toDTO).toList();
    }

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
    static LocalDate localDateTimeToLocalDate(LocalDateTime ldt) {
        if (ldt == null) return null;
        return ldt.toLocalDate();
    }

    @Named("grupoToResumen")
    static NinioResponseDTO.GrupoResumenDTO grupoToResumen(Grupo grupo) {
        if (grupo == null) return null;
        NinioResponseDTO.GrupoResumenDTO r = new NinioResponseDTO.GrupoResumenDTO();
        r.setId(grupo.getId());
        r.setNombre(grupo.getNombre());
        r.setRangoEdad(grupo.getRangoEdad());
        r.setHoraInicio(grupo.getHoraInicio() != null ? grupo.getHoraInicio().toString() : null);
        r.setHoraFin(grupo.getHoraFin() != null ? grupo.getHoraFin().toString() : null);
        return r;
    }

    @Named("condicionesToDTO")
    static List<CondicionMedicaResponseDTO> condicionesToDTO(List<CondicionMedica> condiciones) {
        if (condiciones == null) return Collections.emptyList();
        return condiciones.stream().map(c -> {
            CondicionMedicaResponseDTO dto = new CondicionMedicaResponseDTO();
            dto.setCondicionId(c.getCondicionId());
            dto.setCondicion(c.getCondicion());
            dto.setObservacion(c.getObservaciones());
            dto.setEsCronica(c.getEsCronica());
            return dto;
        }).collect(Collectors.toList());
    }
}