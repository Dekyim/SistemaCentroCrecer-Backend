package tip.java.sistemacentrocrecer.biz.dao.specifications;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import tip.java.sistemacentrocrecer.biz.dao.entities.CondicionMedica;
import tip.java.sistemacentrocrecer.biz.dao.entities.Grupo;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;
import tip.java.sistemacentrocrecer.biz.dao.enums.SexoNinioEnum;
import tip.java.sistemacentrocrecer.dto.NinioFilterRequestDTO;

import java.time.ZoneId;
import java.util.Date;

public class NinioSpecification {

    private NinioSpecification() {
    }

    public static Specification<Ninio> conFiltros(NinioFilterRequestDTO filtro) {
        Specification<Ninio> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (tieneTexto(filtro.getNombre())) {
            specification = specification.and(nombreContiene(filtro.getNombre()));
        }

        if (tieneTexto(filtro.getApellido())) {
            specification = specification.and(apellidoContiene(filtro.getApellido()));
        }

        if (tieneTexto(filtro.getCedula())) {
            specification = specification.and(cedulaContiene(filtro.getCedula()));
        }

        if (filtro.getGrupoId() != null) {
            specification = specification.and(perteneceAlGrupo(filtro.getGrupoId()));
        }

        if (filtro.getSexo() != null) {
            specification = specification.and(tieneSexo(filtro.getSexo()));
        }

        if (filtro.getFechaNacimiento() != null) {
            specification = specification.and(nacioEn(filtro.getFechaNacimiento()));
        }

        if (tieneTexto(filtro.getDireccion())) {
            specification = specification.and(direccionContiene(filtro.getDireccion()));
        }

        if (filtro.getActivo() != null) {
            specification = specification.and(estaActivo(filtro.getActivo()));
        }

        if (tieneTexto(filtro.getEnfermedad())) {
            specification = specification.and(tieneEnfermedad(filtro.getEnfermedad()));
        }

        return specification;
    }

    private static Specification<Ninio> nombreContiene(String nombre) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%");
    }

    private static Specification<Ninio> apellidoContiene(String apellido) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("apellido")), "%" + apellido.toLowerCase() + "%");
    }

    private static Specification<Ninio> cedulaContiene(String cedula) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("cedula"), "%" + cedula + "%");
    }

    private static Specification<Ninio> perteneceAlGrupo(Integer grupoId) {
        return (root, query, criteriaBuilder) -> {
            Join<Ninio, Grupo> grupo = root.join("grupo", JoinType.LEFT);
            return criteriaBuilder.equal(grupo.get("id"), grupoId);
        };
    }

    private static Specification<Ninio> tieneSexo(SexoNinioEnum sexo) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("sexo"), sexo);
    }

    private static Specification<Ninio> nacioEn(java.time.LocalDate fechaNacimiento) {
        return (root, query, criteriaBuilder) -> {
            Date desde = Date.from(fechaNacimiento.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date hasta = Date.from(fechaNacimiento.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

            return criteriaBuilder.and(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("fechaNacimiento"), desde),
                    criteriaBuilder.lessThan(root.get("fechaNacimiento"), hasta)
            );
        };
    }

    private static Specification<Ninio> direccionContiene(String direccion) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("direccion")), "%" + direccion.toLowerCase() + "%");
    }

    private static Specification<Ninio> estaActivo(Boolean activo) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("activo"), activo);
    }

    private static Specification<Ninio> tieneEnfermedad(String enfermedad) {
        return (root, query, criteriaBuilder) -> {
            if (query != null) {
                query.distinct(true);
            }

            Join<Ninio, CondicionMedica> condicion = root.join("condiciones", JoinType.LEFT);

            return criteriaBuilder.like(
                    criteriaBuilder.lower(condicion.get("condicion")),
                    "%" + enfermedad.toLowerCase() + "%"
            );
        };
    }

    private static boolean tieneTexto(String valor) {
        return valor != null && !valor.isBlank();
    }
}
