package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.Funcionario;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepository
        extends JpaRepository<Funcionario, Integer>, JpaSpecificationExecutor<Funcionario> {
    //Busquedas
    Optional<Funcionario> findByCedula(String cedula);
    Optional<Funcionario> findByEmail(String email);
    Optional<Funcionario> findByNombreAndApellido(String nombre, String apellido);

    //Validaciones
    boolean existsByCedula(String cedula);
    boolean existsByEmailIgnoreCase(String email);
    //Verificar por la cedula y no por otro
    boolean existsByCedulaAndIdNot(String cedula, Integer id);
    //Verificar por el email y no por otro
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Integer id);

    //Listados
    List<Funcionario> findByActivoTrue();
    Page<Funcionario> findByActivoTrue(Pageable pageable);
    List<Funcionario> findByRolIdAndActivoTrue(Integer rolId);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Integer id);
}