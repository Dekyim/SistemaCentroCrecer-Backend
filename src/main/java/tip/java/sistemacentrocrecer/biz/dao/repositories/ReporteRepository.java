package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.Reporte;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReporteRepository
        extends JpaRepository<Reporte, Integer>, JpaSpecificationExecutor<Reporte> {
    // Listados
    List<Reporte> findByActivoTrue();
    List<Reporte> findByFuncionario_Id(Integer funcionarioId);
    Page<Reporte> findByFuncionario_IdAndActivoTrue(Integer funcionarioId, Pageable pageable);
    List<Reporte> findByVistoFalseAndActivoTrue();

    // Por niño
    @Query("SELECT DISTINCT r FROM Reporte r JOIN r.reporteNinios rn WHERE rn.ninio.id = :ninioId")
    List<Reporte> findByNinioId(@Param("ninioId") Integer ninioId);

    @Query("SELECT DISTINCT r FROM Reporte r JOIN r.reporteNinios rn WHERE rn.ninio.id = :ninioId AND r.activo = true")
    List<Reporte> findByNinioIdAndActivoTrue(@Param("ninioId") Integer ninioId);

    // Por grupo
    @Query("SELECT DISTINCT r FROM Reporte r JOIN r.reporteGrupos rg WHERE rg.grupo.id = :grupoId")
    List<Reporte> findByGrupoId(@Param("grupoId") Integer grupoId);

    @Query("SELECT DISTINCT r FROM Reporte r JOIN r.reporteGrupos rg WHERE rg.grupo.id = :grupoId AND r.activo = true")
    List<Reporte> findByGrupoIdAndActivoTrue(@Param("grupoId") Integer grupoId);

    // Fechas
    List<Reporte> findByFechaGeneracionBetweenAndActivoTrueOrderByFechaGeneracionDesc(
            LocalDateTime desde,
            LocalDateTime hasta
    );

    // Por responsable via niño asignado directamente al reporte
    @Query("SELECT DISTINCT r FROM Reporte r" +
            " JOIN r.reporteNinios rn" +
            " JOIN rn.ninio n" +
            " JOIN n.responsables rsp" +
            " WHERE r.activo = true AND rsp.responsable.id = :responsableId" +
            " ORDER BY r.fechaGeneracion DESC")
    List<Reporte> findActivosByResponsableIdViaNinio(@Param("responsableId") Integer responsableId);

    // Por responsable via grupo del niño asociado al reporte
    @Query("SELECT DISTINCT r FROM Reporte r" +
            " JOIN r.reporteGrupos rg" +
            " JOIN rg.grupo g" +
            " JOIN g.ninios n2" +
            " JOIN n2.responsables rsp2" +
            " WHERE r.activo = true AND rsp2.responsable.id = :responsableId" +
            " ORDER BY r.fechaGeneracion DESC")
    List<Reporte> findActivosByResponsableIdViaGrupo(@Param("responsableId") Integer responsableId);

    // Métricas
    Long countByActivoTrueAndVistoFalse();
}