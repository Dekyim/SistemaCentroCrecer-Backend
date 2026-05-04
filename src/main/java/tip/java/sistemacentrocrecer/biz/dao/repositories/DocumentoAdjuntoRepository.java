package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.DocumentoAdjunto;


import java.util.Date;
import java.util.List;

@Repository
public interface DocumentoAdjuntoRepository extends JpaRepository<DocumentoAdjunto, Integer>, JpaSpecificationExecutor<DocumentoAdjunto> {

    // por reporte
    List<DocumentoAdjunto> findByReporteId(Integer reporteId);

    // por tipo
    List<DocumentoAdjunto> findByTipoArchivo(String tipoArchivo);

    // por fecha
    List<DocumentoAdjunto> findByFechaSubida(Date fechaSubida);
    List<DocumentoAdjunto> findByFechaSubidaBetween(Date desde, Date hasta);

    // validación
    boolean existsByReporteIdAndNombreArchivo(Integer reporteId, String nombreArchivo);
}