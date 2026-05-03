package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.Rol;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer>, JpaSpecificationExecutor<Rol> {
    //Ignore case para ignorar mayusculas y minusculas.
    Optional<Rol> findByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
    //Evitar duplicados
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Integer id);
    List<Rol> findByPadreIsNull();
    List<Rol> findByPadreId(Integer padreId);
    List<Rol> findByActivoTrue();
    List<Rol> findByActivoFalse();

}