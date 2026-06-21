package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.DiaNoLaborable;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaNoLaborableRepository extends JpaRepository<DiaNoLaborable, Integer> {

    List<DiaNoLaborable> findByFechaBetweenAndActivoTrueOrderByFechaAsc(
            LocalDate desde,
            LocalDate hasta
    );

    boolean existsByFechaAndActivoTrue(LocalDate fecha);

    boolean existsByFechaAndActivoTrueAndIdNot(LocalDate fecha, Integer id);
}
