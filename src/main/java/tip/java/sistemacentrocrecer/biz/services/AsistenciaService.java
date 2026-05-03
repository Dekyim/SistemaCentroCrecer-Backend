package tip.java.sistemacentrocrecer.biz.services;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.repositories.AsistenciaRepository;

@Service
@AllArgsConstructor
public class AsistenciaService {
    private final AsistenciaRepository asistenciaRepository;
}
