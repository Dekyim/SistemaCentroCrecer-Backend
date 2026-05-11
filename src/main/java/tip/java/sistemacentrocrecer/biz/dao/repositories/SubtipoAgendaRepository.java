package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.SubtipoAgenda;


import java.util.Optional;

@Repository
public interface SubtipoAgendaRepository extends JpaRepository<SubtipoAgenda, Integer>, JpaSpecificationExecutor<SubtipoAgenda> {

    // por nombre
    Optional<SubtipoAgenda> findBySubtipo(String subtipo);

    // validaciones
    boolean existsBySubtipo(String subtipo);

}