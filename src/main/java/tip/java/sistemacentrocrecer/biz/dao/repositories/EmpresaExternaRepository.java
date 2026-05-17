package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tip.java.sistemacentrocrecer.biz.dao.entities.EmpresaExterna;

import java.util.List;
import java.util.Optional;

public interface EmpresaExternaRepository
        extends JpaRepository<EmpresaExterna, Integer>, JpaSpecificationExecutor<EmpresaExterna> {
    Optional<EmpresaExterna> findById(Integer id);

    List<EmpresaExterna> findByActividadId(Integer actividadId);

    List<EmpresaExterna> findByActividadIsNull();

    List<EmpresaExterna> findByTipoServicioIgnoreCase(String tipoServicio);

    boolean existsByNombreAndActividadId(String nombre, Integer actividadId);
}
