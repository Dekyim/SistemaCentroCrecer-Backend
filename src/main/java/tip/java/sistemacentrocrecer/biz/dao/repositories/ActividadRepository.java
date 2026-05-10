package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.Actividad;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Integer> {
    Optional<Actividad> findById(Integer id);

    List<Actividad> findByActivoTrue();
}
