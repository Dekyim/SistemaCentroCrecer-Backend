package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;
import tip.java.sistemacentrocrecer.biz.dao.entities.ResponsableNinio;

import java.util.List;
import java.util.Optional;

public interface ResponsableNinioRepository extends JpaRepository<ResponsableNinio, Integer>, JpaSpecificationExecutor<ResponsableNinio> {
    Optional<ResponsableNinio> findByResponsableCedula(String cedula);
    Optional<ResponsableNinio> findByNinioCedula(String cedula);

    /**
     * Paso 1: trae los niños del responsable con grupo + responsables.
     * (Un solo JOIN FETCH de colección para evitar MultipleBagFetchException)
     */
    @Query("SELECT DISTINCT n FROM ResponsableNinio rn " +
            "JOIN rn.ninio n " +
            "LEFT JOIN FETCH n.grupo " +
            "LEFT JOIN FETCH n.responsables rn2 " +
            "LEFT JOIN FETCH rn2.responsable " +
            "WHERE rn.responsable.id = :responsableId")
    List<Ninio> findNiniosByResponsableId(@Param("responsableId") Integer responsableId);

    /**
     * Paso 2: inicializa condiciones médicas para los niños ya cargados.
     */
    @Query("SELECT DISTINCT n FROM Ninio n " +
            "LEFT JOIN FETCH n.condiciones " +
            "WHERE n IN :ninios")
    List<Ninio> findNiniosWithCondiciones(@Param("ninios") List<Ninio> ninios);

    boolean existsByNinioIdAndResponsableId(Integer ninioId, Integer responsableId);

    List<ResponsableNinio> findByNinioId(Integer ninioId);
}