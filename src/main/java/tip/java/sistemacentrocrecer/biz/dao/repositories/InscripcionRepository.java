package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tip.java.sistemacentrocrecer.biz.dao.entities.Inscripcion;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;

import java.util.Optional;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Integer> {
    Optional<Inscripcion> findById(Integer id);

}
