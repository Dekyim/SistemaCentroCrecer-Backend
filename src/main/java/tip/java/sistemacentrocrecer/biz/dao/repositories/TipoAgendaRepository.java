package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.TipoAgenda;


import java.util.Optional;

@Repository
public interface TipoAgendaRepository extends JpaRepository<TipoAgenda, Integer>, JpaSpecificationExecutor<TipoAgenda> {

    // por nombre
    Optional<TipoAgenda> findByTipo(String tipo);

    // validaciones
    boolean existsByTipo(String tipo);
}