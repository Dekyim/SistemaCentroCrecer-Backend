package tip.java.sistemacentrocrecer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import tip.java.sistemacentrocrecer.biz.dao.entities.Inscripcion;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;
import tip.java.sistemacentrocrecer.biz.dao.entities.Responsable;
import tip.java.sistemacentrocrecer.dto.InscripcionSolicitudResponseDTO;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", uses = {CondicionMedicaMapper.class})
public interface InscripcionSolicitudMapper {

    @Mapping(target = "responsableId",       source = "inscripcion", qualifiedByName = "responsableId")
    @Mapping(target = "responsableNombre",   source = "inscripcion", qualifiedByName = "responsableNombre")
    @Mapping(target = "responsableCedula",   source = "inscripcion", qualifiedByName = "responsableCedula")
    @Mapping(target = "responsableEmail",    source = "inscripcion", qualifiedByName = "responsableEmail")
    @Mapping(target = "responsableTelefono", source = "inscripcion", qualifiedByName = "responsableTelefono")
    @Mapping(target = "ninioId",             source = "ninio.id")
    @Mapping(target = "ninioNombre",         source = "ninio.nombre")
    @Mapping(target = "ninioApellido",       source = "ninio.apellido")
    @Mapping(target = "ninioCedula",         source = "ninio.cedula")
    @Mapping(target = "ninioSexo",           source = "ninio.sexo")
    @Mapping(target = "ninioObservaciones",  source = "ninio.observaciones")
    @Mapping(target = "ninioDireccion",      source = "ninio.direccion")
    @Mapping(target = "ninioFechaNacimiento",source = "ninio.fechaNacimiento", qualifiedByName = "dateToLocalDate")
    @Mapping(target = "condicionesMedicas",  source = "ninio.condiciones")
    @Mapping(target = "grupoId",             source = "ninio.grupo.id")
    @Mapping(target = "grupoNombre",         source = "ninio.grupo.nombre")
    InscripcionSolicitudResponseDTO toDTO(Inscripcion inscripcion);

    default List<InscripcionSolicitudResponseDTO> toDTOList(List<Inscripcion> inscripciones) {
        if (inscripciones == null) return Collections.emptyList();
        return inscripciones.stream().map(this::toDTO).toList();
    }

    @Named("responsableId")
    default Integer responsableId(Inscripcion i) {
        Responsable r = primerResponsable(i);
        return r != null ? r.getId() : null;
    }

    @Named("responsableNombre")
    default String responsableNombre(Inscripcion i) {
        Responsable r = primerResponsable(i);
        return r != null ? r.getNombre() + " " + r.getApellido() : null;
    }

    @Named("responsableCedula")
    default String responsableCedula(Inscripcion i) {
        Responsable r = primerResponsable(i);
        return r != null ? r.getCedula() : null;
    }

    @Named("responsableEmail")
    default String responsableEmail(Inscripcion i) {
        Responsable r = primerResponsable(i);
        return r != null ? r.getEmail() : null;
    }

    @Named("responsableTelefono")
    default String responsableTelefono(Inscripcion i) {
        Responsable r = primerResponsable(i);
        return r != null ? r.getTelefono() : null;
    }

    default Responsable primerResponsable(Inscripcion i) {
        // Intenta primero desde la tabla responsable_inscripcion
        if (i.getResponsables() != null && !i.getResponsables().isEmpty()) {
            return i.getResponsables().get(0);
        }
        // Fallback: busca desde ResponsableNinio del niño
        if (i.getNinio() != null
                && i.getNinio().getResponsables() != null
                && !i.getNinio().getResponsables().isEmpty()) {
            var rn = i.getNinio().getResponsables().get(0);
            return rn != null ? rn.getResponsable() : null;
        }
        return null;
    }

    @Named("dateToLocalDate")
    default LocalDate dateToLocalDate(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}