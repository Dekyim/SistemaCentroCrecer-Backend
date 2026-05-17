package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.Actividad;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Integer> {
    Optional<Actividad> findById(Integer id);

    List<Actividad> findByActivoTrue();

    List<Actividad> findByActivoTrueAndFechaDesdeGreaterThanEqual(LocalDate desde);

    List<Actividad> findByActivoTrueAndFechaDesdeGreaterThanEqualAndFechaHastaLessThanEqual(LocalDate desde, LocalDate hasta);

    List<Actividad> findByNiniosId(Integer ninioId);

    List<Actividad> findByEmpresasExternasId(Integer empresaId);

    List<Actividad> findByActivoTrueAndFechaDesdeLessThanEqualAndFechaHastaGreaterThanEqual(LocalDate fechaDesde, LocalDate fechaHasta);

    List<Actividad> findByActivoTrueAndFechaDesdeGreaterThan(LocalDate desde);
}
