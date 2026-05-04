package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.Asistencia;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AsistenciaRepository
        extends JpaRepository<Asistencia, Integer>, JpaSpecificationExecutor<Asistencia>  {
    //Basicos
    List<Asistencia> findByNinio_Id(Integer ninioId);
    Page<Asistencia> findByNinio_IdAndActivoTrue(Integer ninioId, Pageable pageable); //para implementar paginacion y ordenamiento de registros
    List<Asistencia> findByFuncionario_Id(Integer funcionarioId);
    List<Asistencia> findByFecha(LocalDate fecha);
    Optional<Asistencia> findByNinio_Cedula(String cedula);
    List<Asistencia> findByFechaBetweenAndActivoTrue(LocalDate desde, LocalDate hasta);
    Optional<Asistencia> findByNinio_IdAndFecha(Integer ninioId, LocalDate fecha);
    boolean existsByNinio_IdAndFechaAndActivoTrue(Integer ninioId, LocalDate fecha);

    //Para periodos
    List<Asistencia> findByNinio_IdAndFechaBetweenAndActivoTrueOrderByFechaDesc(
            Integer ninioId,
            LocalDate desde,
            LocalDate hasta
    );
    Long countByNinio_IdAndFechaBetweenAndActivoTrue(
            Integer ninioId,
            LocalDate desde,
            LocalDate hasta
    );

}
