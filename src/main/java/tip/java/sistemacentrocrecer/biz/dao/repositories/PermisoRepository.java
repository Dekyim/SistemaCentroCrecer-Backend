package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tip.java.sistemacentrocrecer.biz.dao.entities.Permiso;

import java.util.List;
import java.util.Optional;

public interface PermisoRepository
        extends JpaRepository<Permiso, Integer>, JpaSpecificationExecutor<Permiso> {
    Optional<Permiso> findById(Integer id);
    List<Permiso> findByActivoTrue();

    Optional<Permiso> findByNinioCedula(String ninioCedula);

    List<Permiso> findByActividadId(Integer actividadId);

    List<Permiso> findByActividadIdAndAutorizado(Integer actividadId, Boolean autorizado);

    Optional<Permiso> findByActividadIdAndNinioId(Integer actividadId, Integer ninioId);
    boolean existsByActividadIdAndNinioId(Integer actividadId, Integer ninioId);
}
