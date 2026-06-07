package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.Notificacion;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    List<Notificacion> findByFuncionario_IdOrderByFechaCreacionDesc(Integer funcionarioId);

    List<Notificacion> findByFuncionario_IdAndLeidaFalseOrderByFechaCreacionDesc(Integer funcionarioId);

    Long countByFuncionario_IdAndLeidaFalse(Integer funcionarioId);

    @Modifying
    @Query("UPDATE Notificacion n SET n.leida = true WHERE n.funcionario.id = :funcionarioId AND n.leida = false")
    void marcarTodasComoLeidas(@Param("funcionarioId") Integer funcionarioId);
}