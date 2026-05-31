package tip.java.sistemacentrocrecer.biz.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tip.java.sistemacentrocrecer.biz.dao.entities.CondicionMedica;

import java.util.List;

public interface CondicionMedicaRepository extends JpaRepository<CondicionMedica, Integer> {

    List<CondicionMedica> findByNinioId(Integer ninioId);
}