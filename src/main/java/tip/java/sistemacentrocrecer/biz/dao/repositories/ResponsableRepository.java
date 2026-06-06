package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tip.java.sistemacentrocrecer.biz.dao.entities.Responsable;

import java.util.List;
import java.util.Optional;

public interface ResponsableRepository extends JpaRepository<Responsable, Integer> {

    Optional<Responsable> findByCedula(String cedula);

    Optional<Responsable> findByEmail(String email);

    List<Responsable> findByActivoTrue();
}