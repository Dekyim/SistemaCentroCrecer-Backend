package tip.java.sistemacentrocrecer.biz.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.repositories.FuncionarioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.RolRepository;

@Service
@AllArgsConstructor

public class FuncionarioService {
    private final FuncionarioRepository funcionarioRepository;
    private final RolRepository rolRepository;

}
