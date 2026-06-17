package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.Turno;


import java.time.LocalTime;
import java.util.List;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Integer>, JpaSpecificationExecutor<Turno> {

    // activos
    List<Turno> findByActivoTrue();
    List<Turno> findByActivoFalse();

    // baja lógica
    List<Turno> findByFechaBajaIsNull();

    // por funcionario
    List<Turno> findByFuncionarioId(Integer funcionarioId);
    List<Turno> findByFuncionarioIdAndActivoTrue(Integer funcionarioId);

    // por horario
    List<Turno> findByHoraInicio(LocalTime horaInicio);
    List<Turno> findByHoraInicioBetween(LocalTime desde, LocalTime hasta);

    // validaciones
    boolean existsByFuncionarioIdAndHoraInicioAndHoraFin(Integer funcionarioId, LocalTime horaInicio, LocalTime horaFin);
    boolean existsByFuncionarioIdAndHoraInicioAndHoraFinAndIdNot(Integer funcionarioId, LocalTime horaInicio, LocalTime horaFin, Integer id);

    long countByActivoTrue();

    // por rol del funcionario
    List<Turno> findByFuncionario_Rol_NombreIgnoreCase(String rolNombre);
}