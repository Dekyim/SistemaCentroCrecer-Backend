package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.AgendaLimpieza;
import tip.java.sistemacentrocrecer.biz.dao.enums.EstadoLimpiezaEnum;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendaLimpiezaRepository extends JpaRepository<AgendaLimpieza, Integer>, JpaSpecificationExecutor<AgendaLimpieza> {

    List<AgendaLimpieza> findByEstado(EstadoLimpiezaEnum estado);

    List<AgendaLimpieza> findByFuncionarioId(Integer funcionarioId);

    Optional<AgendaLimpieza> findBySubtipoAgendaSubtipoId(Integer subtipoId);

    List<AgendaLimpieza> findByFuncionarioIdAndEstado(Integer funcionarioId, EstadoLimpiezaEnum estado);

    @Query(value =
            "SELECT COUNT(a.id) > 0 FROM agendas_limpiezas a " +
                    "WHERE a.funcionario_id = :funcionarioId " +
                    "AND a.fecha = :fecha " +
                    "AND a.hora_inicio IS NOT NULL " +
                    "AND CAST(:horaInicio AS time) < COALESCE(a.hora_fin, a.hora_inicio) " +
                    "AND COALESCE(CAST(:horaFin AS time), CAST(:horaInicio AS time)) > a.hora_inicio",
            nativeQuery = true)
    boolean existsSolapamiento(
            @Param("funcionarioId") Integer funcionarioId,
            @Param("fecha") LocalDate fecha,
            @Param("horaInicio") String horaInicio,
            @Param("horaFin") String horaFin
    );

    @Query(value =
            "SELECT COUNT(a.id) > 0 FROM agendas_limpiezas a " +
                    "WHERE a.funcionario_id = :funcionarioId " +
                    "AND a.fecha = :fecha " +
                    "AND a.id <> :excludeId " +
                    "AND a.hora_inicio IS NOT NULL " +
                    "AND CAST(:horaInicio AS time) < COALESCE(a.hora_fin, a.hora_inicio) " +
                    "AND COALESCE(CAST(:horaFin AS time), CAST(:horaInicio AS time)) > a.hora_inicio",
            nativeQuery = true)
    boolean existsSolapamientoExcluyendo(
            @Param("funcionarioId") Integer funcionarioId,
            @Param("fecha") LocalDate fecha,
            @Param("horaInicio") String horaInicio,
            @Param("horaFin") String horaFin,
            @Param("excludeId") Integer excludeId
    );
}
