package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tip.java.sistemacentrocrecer.biz.dao.entities.Inscripcion;
import tip.java.sistemacentrocrecer.biz.dao.enums.EstadoInscripcionEnum;

import java.util.List;
import java.util.Optional;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Integer> {

    Optional<Inscripcion> findById(Integer id);

    List<Inscripcion> findByEstadoInscripcion(EstadoInscripcionEnum estado);

    List<Inscripcion> findByNinioId(Integer ninioId);

    @Query("SELECT i FROM Inscripcion i JOIN i.responsables r WHERE r.id = :responsableId")
    List<Inscripcion> findByResponsableId(@Param("responsableId") Integer responsableId);
}