package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.Asistencia;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AsistenciaRepository
        extends JpaRepository<Asistencia, Integer>, JpaSpecificationExecutor<Asistencia>  {

    List<Asistencia> findByNinio_Id(Integer ninioId);
    Page<Asistencia> findByNinio_IdAndActivoTrue(Integer ninioId, Pageable pageable);
    List<Asistencia> findByFuncionario_Id(Integer funcionarioId);
    List<Asistencia> findByFecha(LocalDate fecha);
    Optional<Asistencia> findByNinio_Cedula(String cedula);
    List<Asistencia> findByFechaBetweenAndActivoTrue(LocalDate desde, LocalDate hasta);
    Optional<Asistencia> findByNinio_IdAndFecha(Integer ninioId, LocalDate fecha);
    boolean existsByNinio_IdAndFechaAndActivoTrue(Integer ninioId, LocalDate fecha);

    List<Asistencia> findByNinio_IdInAndFechaAndActivoTrue(List<Integer> ninioIds, LocalDate fecha);

    List<Asistencia> findByNinio_IdAndFechaBetweenAndActivoTrueOrderByFechaDesc(
            Integer ninioId,
            LocalDate desde,
            LocalDate hasta
    );
    Long countByNinio_IdAndFechaBetweenAndActivoTrue(
            Integer ninioId,
            LocalDate desde,
            LocalDate hasta
    );


    List<Asistencia> findByNinio_Grupo_IdAndFechaAndActivoTrue(Integer grupoId, LocalDate fecha);

    @Query("SELECT a FROM Asistencia a " +
            "JOIN a.ninio n " +
            "JOIN n.grupo g " +
            "JOIN g.funcionarios f " +
            "WHERE f.id = :funcionarioId AND a.fecha = :fecha AND a.activo = true")
    List<Asistencia> findAsistenciasDeNiniosPorFuncionarioYFecha(
            @Param("funcionarioId") Integer funcionarioId,
            @Param("fecha") LocalDate fecha
    );

    @Query("SELECT COUNT(n) > 0 FROM Ninio n " +
            "JOIN n.grupo g " +
            "JOIN g.funcionarios f " +
            "WHERE n.id = :ninioId AND f.id = :funcionarioId AND n.activo = true AND g.activo = true")
    boolean ninioPerteneceFuncionario(
            @Param("ninioId") Integer ninioId,
            @Param("funcionarioId") Integer funcionarioId
    );

    @Query("SELECT a FROM Asistencia a " +
            "JOIN a.ninio n " +
            "JOIN n.grupo g " +
            "JOIN g.funcionarios f " +
            "WHERE f.id = :funcionarioId AND a.fecha = :fecha " +
            "ORDER BY g.nombre, n.nombre")
    List<Asistencia> findAsistenciasPorFuncionarioFecha(
            @Param("funcionarioId") Integer funcionarioId,
            @Param("fecha") LocalDate fecha
    );

    @Query("SELECT a FROM Asistencia a " +
            "JOIN a.ninio n " +
            "WHERE n.cedula = :cedula AND a.ninio IS NOT NULL " +
            "ORDER BY a.fecha DESC")
    List<Asistencia> findHistorialPorCedulaNinio(@Param("cedula") String cedula);

    @Query("SELECT COUNT(DISTINCT a.fecha) FROM Asistencia a " +
            "WHERE a.ninio IS NOT NULL AND a.activo = true " +
            "AND a.fecha BETWEEN :desde AND :hasta")
    long countDiasConAsistenciaEnPeriodo(
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta
    );

    @Query("SELECT a FROM Asistencia a " +
            "WHERE a.funcionario IS NOT NULL AND a.ninio IS NULL " +
            "AND a.fecha BETWEEN :desde AND :hasta " +
            "AND a.activo = true " +
            "ORDER BY a.fecha DESC, a.funcionarioNombre ASC")
    List<Asistencia> findAsistenciasFuncionariosPorRango(
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta
    );
}