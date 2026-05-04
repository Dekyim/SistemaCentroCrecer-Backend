package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;

public interface NinioRepository
        extends JpaRepository <Ninio, Integer>, JpaSpecificationExecutor<Ninio> {
}
