package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.AgendaLimpieza;
import tip.java.sistemacentrocrecer.biz.dao.enums.EstadoLimpiezaEnum;


import java.util.List;
import java.util.Optional;

@Repository
public interface AgendaLimpiezaRepository extends JpaRepository<AgendaLimpieza, Integer>, JpaSpecificationExecutor<AgendaLimpieza> {

    // por estado
    List<AgendaLimpieza> findByEstado(EstadoLimpiezaEnum estado);

    // por agenda
    List<AgendaLimpieza> findByAgendaId(Integer agendaId);

    // por subtipo
    Optional<AgendaLimpieza> findBySubtipoAgendaSubtipoId(Integer subtipoId);

    // combinados
    List<AgendaLimpieza> findByAgendaIdAndEstado(Integer agendaId, EstadoLimpiezaEnum estado);

    // validación
    boolean existsByAgendaIdAndSubtipoAgendaSubtipoId(Integer agendaId, Integer subtipoId);
    boolean existsByAgendaIdAndSubtipoAgendaSubtipoIdAndIdNot(Integer agendaId, Integer subtipoId, Integer id);

    List<AgendaLimpieza> findByAgendaFuncionarioId(Integer funcionarioId);
}