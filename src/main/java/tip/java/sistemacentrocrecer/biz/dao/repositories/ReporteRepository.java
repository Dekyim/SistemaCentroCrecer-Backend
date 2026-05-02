package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.Reporte;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReporteRepository
        extends JpaRepository<Reporte, Integer>, JpaSpecificationExecutor<Reporte> {
    //Listados
    Page<Reporte> findByActivoTrue(Pageable pageable);
    List<Reporte> findByFuncionario_Id(Integer funcionarioId);
    Page<Reporte> findByFuncionario_IdAndActivoTrue(Integer funcionarioId, Pageable pageable);
    List<Reporte> findByVistoFalseAndActivoTrue();

    //Fechas
    List<Reporte> findByFechaGeneracionBetweenAndActivoTrueOrderByFechaGeneracionDesc(
            LocalDateTime desde,
            LocalDateTime hasta
    );

    //Metricas
    Long countByActivoTrueAndVistoFalse();
}
