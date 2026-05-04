package tip.java.sistemacentrocrecer.biz.services;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tip.java.sistemacentrocrecer.biz.dao.entities.Asistencia;
import tip.java.sistemacentrocrecer.biz.dao.entities.Funcionario;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;
import tip.java.sistemacentrocrecer.biz.dao.repositories.AsistenciaRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.FuncionarioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.NinioRepository;
import tip.java.sistemacentrocrecer.dto.AsistenciaRequestDTO;
import tip.java.sistemacentrocrecer.dto.AsistenciaResponseDTO;
import tip.java.sistemacentrocrecer.exceptions.BusinessException;
import tip.java.sistemacentrocrecer.exceptions.CedulaNotFoundException;
import tip.java.sistemacentrocrecer.exceptions.ResourceNotFoundException;
import tip.java.sistemacentrocrecer.mapper.AsistenciaMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class AsistenciaService {
    private final AsistenciaRepository asistenciaRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final NinioRepository ninioRepository;
    private final AsistenciaMapper asistenciaMapper;

    public List<AsistenciaResponseDTO> listarTodos() {
        return asistenciaRepository.findAll().stream()
                .map(asistenciaMapper::toResponseDTO)
                .toList();
    }

    public AsistenciaResponseDTO obtenerPorId(Integer id) {
        Asistencia a = asistenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asistencia", id));

        return asistenciaMapper.toResponseDTO(a);
    }
    public AsistenciaResponseDTO obtenerPorCedula(String cedula) {
        Asistencia a = asistenciaRepository.findByNinio_Cedula(cedula)
                .orElseThrow(() -> new CedulaNotFoundException(cedula));

        return asistenciaMapper.toResponseDTO(a);
    }

    @Transactional
    public AsistenciaResponseDTO crear(AsistenciaRequestDTO dto) {

        if (dto.getHoraSalida().isBefore(dto.getHoraEntrada())) {
            throw new BusinessException("La hora de salida no puede ser menor que la de entrada");
        }

        Ninio ninio = ninioRepository.findById(dto.getNinioId())
                .orElseThrow(() -> new ResourceNotFoundException("Niño", dto.getNinioId()));

        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario", dto.getFuncionarioId()));

        Asistencia asistencia = asistenciaMapper.toEntity(dto);

        //relaciones
        asistencia.setNinio(ninio);
        asistencia.setFuncionario(funcionario);

        //datos desnormalizados
        asistencia.setNinioNombre(ninio.getNombre());
        asistencia.setNinioCedula(ninio.getCedula());

        asistencia.setFuncionarioNombre(funcionario.getNombre());
        asistencia.setFuncionarioCedula(funcionario.getCedula());

        return asistenciaMapper.toResponseDTO(asistenciaRepository.save(asistencia));
    }

    @Transactional
    public void darDeBaja(Integer id) {
        Asistencia a = asistenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asistencia", id));

        if (!a.getActivo()) {
            throw new BusinessException("La asistencia ya está inactiva");
        }

        a.setActivo(false);
        asistenciaRepository.save(a);
    }
}
