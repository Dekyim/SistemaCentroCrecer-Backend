package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.DetalleAgenda;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetalleAgendaRepository extends JpaRepository<DetalleAgenda, Integer>, JpaSpecificationExecutor<DetalleAgenda> {

    // activos
    List<DetalleAgenda> findByActivoTrue();
    List<DetalleAgenda> findByActivoFalse();

    // no dados de baja
    List<DetalleAgenda> findByFechaBajaIsNull();

    // por agenda
    List<DetalleAgenda> findByAgendaId(Integer agendaId);

    // por subtipo
    Optional<DetalleAgenda> findBySubtipoSubtipoId(Integer subtipoId);

    // combinados
    List<DetalleAgenda> findByAgendaIdAndActivoTrue(Integer agendaId);

    // validación
    boolean existsByAgendaIdAndSubtipoSubtipoId(Integer agendaId, Integer subtipoId);
    boolean existsByAgendaIdAndSubtipoSubtipoIdAndIdNot(Integer agendaId, Integer subtipoId, Integer id);
}