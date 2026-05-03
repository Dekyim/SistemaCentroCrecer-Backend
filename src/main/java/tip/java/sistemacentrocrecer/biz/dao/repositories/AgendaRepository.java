package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.Agenda;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Integer>, JpaSpecificationExecutor<Agenda> {

    // buscar activos
    List<Agenda> findByactivoTrue();
    List<Agenda> findByactivoFalse();

    // buscar por fecha
    List<Agenda> findByfecha(LocalDate fecha);
    List<Agenda> findByfechaBetween(LocalDate desde, LocalDate hasta);

    // buscar por id de funcionario
    List<Agenda> findByFuncionario_id(Integer funcionarioId);
    List<Agenda> findByfuncionario_idAndActivoTrue(Integer funcionarioId);

    // buscar por tipo de agenda
    List<Agenda> findByTipoId(Integer tipoId);

    // buscar por fecha y funcionario
    List<Agenda> findByFechaAndFuncionarioId(LocalDate fecha, Integer funcionarioId);

    // para validaciones
    boolean existsByFuncionarioIdAndFechaAndHoraInicioAndHoraFin(
            Integer funcionarioId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin);

    // para validaciones
    boolean existsByFuncionarioIdAndFechaAndHoraInicioAndHoraFinAndIdNot(
            Integer funcionarioId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, Integer id);

    // buscar agendas que no estan dadas de baja, o sea actuales
    List<Agenda> findByFechaBajaIsNull();

}