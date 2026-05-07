package tip.java.sistemacentrocrecer.biz.dao.repositories;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tip.java.sistemacentrocrecer.biz.dao.entities.Responsable;


import java.util.List;
import java.util.Optional;

@Repository
public interface ResponsableRepository extends JpaRepository<Responsable, Integer> {
    Optional<Responsable> findByCedula(String cedula);

    Optional<Responsable> findByEmail(String email);

    List<Responsable> findByActivoTrue();
}
