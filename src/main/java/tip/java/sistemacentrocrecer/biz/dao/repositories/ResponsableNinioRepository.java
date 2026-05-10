package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;
import tip.java.sistemacentrocrecer.biz.dao.entities.ResponsableNinio;

import java.util.Optional;

public interface ResponsableNinioRepository extends JpaRepository<ResponsableNinio, Integer>, JpaSpecificationExecutor<ResponsableNinio> {
    Optional<ResponsableNinio> findByResponsableCedula(String cedula);
    Optional<ResponsableNinio> findByNinioCedula(String cedula);

}
