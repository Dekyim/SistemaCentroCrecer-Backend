package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT p FROM Permiso p " +
            "JOIN p.ninio n " +
            "JOIN n.responsables rn " +
            "WHERE rn.responsable.id = :responsableId " +
            "AND p.activo = true")
    List<Permiso> findByResponsableIdAndActivoTrue(@Param("responsableId") Integer responsableId);}
