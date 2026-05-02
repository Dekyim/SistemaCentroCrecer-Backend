package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.ReporteGrupo;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReporteGrupoRepository
        extends JpaRepository<ReporteGrupo, Integer> {
    //Busquedas
    List<ReporteGrupo> findByReporte_Id(Integer reporteId);
    List<ReporteGrupo> findByGrupo_Id(Integer grupoId);
    Optional<ReporteGrupo> findByReporte_IdAndGrupo_Id(Integer reporteId, Integer grupoId);

    //Validacion
    boolean existsByReporte_IdAndGrupo_Id(Integer reporteId, Integer grupoId);

    //Eliminacion
    void deleteByReporte_IdAndGrupo_Id(Integer reporteId, Integer grupoId);

}
