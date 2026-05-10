package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tip.java.sistemacentrocrecer.biz.dao.entities.EmpresaExterna;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;

import java.util.Optional;

public interface EmpresaExternaRepository
        extends JpaRepository<EmpresaExterna, Integer>, JpaSpecificationExecutor<EmpresaExterna> {
    Optional<EmpresaExterna> findById(Integer id);

}
