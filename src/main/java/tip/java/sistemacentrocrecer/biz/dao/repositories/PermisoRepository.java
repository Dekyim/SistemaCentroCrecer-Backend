package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tip.java.sistemacentrocrecer.biz.dao.entities.Asistencia;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;
import tip.java.sistemacentrocrecer.biz.dao.entities.Permiso;

import java.util.List;
import java.util.Optional;

public interface PermisoRepository
        extends JpaRepository<Permiso, Integer>, JpaSpecificationExecutor<Permiso> {
    Optional<Permiso> findById(Integer id);
    List<Permiso> findByActivoTrue();

    Optional<Permiso> findByNinioCedula(String ninioCedula);

}
