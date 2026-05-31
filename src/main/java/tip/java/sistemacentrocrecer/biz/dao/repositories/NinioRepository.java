package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;

import java.util.List;
import java.util.Optional;

public interface NinioRepository extends JpaRepository<Ninio, Integer>, JpaSpecificationExecutor<Ninio> {
    Optional<Ninio> findByCedula(String cedula);

    /** Niños activos que pertenecen a grupos donde participa el funcionario */
    @Query("SELECT n FROM Ninio n " +
            "JOIN n.grupo g " +
            "JOIN g.funcionarios f " +
            "WHERE f.id = :funcionarioId AND n.activo = true AND g.activo = true " +
            "ORDER BY g.nombre, n.nombre")
    List<Ninio> findNiniosByFuncionarioId(@Param("funcionarioId") Integer funcionarioId);
}