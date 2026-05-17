package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.Grupo;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GrupoRepository
        extends JpaRepository<Grupo, Integer>, JpaSpecificationExecutor<Grupo> {
    //Busquedas
    Optional<Grupo> findByNombreIgnoreCase(String nombre);
    List<Grupo> findByActivoTrue();
    Page<Grupo> findByActivoTrue(Pageable pageable);
    List<Grupo> findByRangoEdadAndActivoTrue(String rangoEdad);

    //Validaciones
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Integer id);

    //Horarios
    //Contenido dentro del rango
    List<Grupo> findByHoraInicioGreaterThanEqualAndHoraFinLessThanEqualAndActivoTrue(
            LocalTime desde,
            LocalTime hasta
    );

    long countByActivoTrue();
}
