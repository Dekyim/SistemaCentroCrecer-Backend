package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface NinioRepository extends JpaRepository<Ninio, Integer>, JpaSpecificationExecutor<Ninio> {

    Optional<Ninio> findByCedula(String cedula);

    /**
     * Paso 1: trae todos los niños con grupo + responsables.
     * (Solo un JOIN FETCH de colección por query para evitar MultipleBagFetchException)
     */
    @Query("SELECT DISTINCT n FROM Ninio n " +
            "LEFT JOIN FETCH n.grupo " +
            "LEFT JOIN FETCH n.responsables rn " +
            "LEFT JOIN FETCH rn.responsable")
    List<Ninio> findAllWithResponsables();

    /**
     * Paso 2: trae todos los niños con sus condiciones médicas.
     * Se llama en NinioService para inicializar condiciones en una segunda query.
     */
    @Query("SELECT DISTINCT n FROM Ninio n " +
            "LEFT JOIN FETCH n.condiciones " +
            "WHERE n IN :ninios")
    List<Ninio> findAllWithCondiciones(@Param("ninios") List<Ninio> ninios);

    /**
     * Trae un niño por id con sus responsables inicializados.
     */
    @Query("SELECT n FROM Ninio n " +
            "LEFT JOIN FETCH n.grupo " +
            "LEFT JOIN FETCH n.responsables rn " +
            "LEFT JOIN FETCH rn.responsable " +
            "WHERE n.id = :id")
    Optional<Ninio> findByIdWithResponsables(@Param("id") Integer id);

    /**
     * Trae un niño por id con sus condiciones médicas.
     */
    @Query("SELECT n FROM Ninio n " +
            "LEFT JOIN FETCH n.condiciones " +
            "WHERE n.id = :id")
    Optional<Ninio> findByIdWithCondiciones(@Param("id") Integer id);

    /** Niños activos que pertenecen a grupos donde participa el funcionario */
    @Query("SELECT n FROM Ninio n " +
            "JOIN n.grupo g " +
            "JOIN g.funcionarios f " +
            "WHERE f.id = :funcionarioId AND n.activo = true AND g.activo = true " +
            "ORDER BY g.nombre, n.nombre")
    List<Ninio> findNiniosByFuncionarioId(@Param("funcionarioId") Integer funcionarioId);

    List<Ninio> findByGrupoIdInAndActivoTrue(Collection<Integer> grupoIds);
}
