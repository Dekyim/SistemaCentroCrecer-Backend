package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.ReporteNinio;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReporteNinioRepository
        extends JpaRepository<ReporteNinio, Integer> {
    //Busquedas
    List<ReporteNinio> findByReporte_Id(Integer reporteId);
    List<ReporteNinio> findByNinio_Id(Integer ninioId);
    Optional<ReporteNinio> findByReporte_IdAndNinio_Id(Integer reporteId, Integer ninioId);

    //Validaciones
    boolean existsByReporte_IdAndNinio_Id(Integer reporteId, Integer ninioId);

    //Eliminacion
    void deleteByReporte_IdAndNinio_Id(Integer reporteId, Integer ninioId);

    //Metricas
    Long countByNinio_Id(Integer ninioId);

}
