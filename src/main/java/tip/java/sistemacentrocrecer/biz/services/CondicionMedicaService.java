package tip.java.sistemacentrocrecer.biz.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tip.java.sistemacentrocrecer.biz.dao.entities.CondicionMedica;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;
import tip.java.sistemacentrocrecer.biz.dao.repositories.CondicionMedicaRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.NinioRepository;
import tip.java.sistemacentrocrecer.dto.CondicionMedicaRequestDTO;
import tip.java.sistemacentrocrecer.dto.CondicionMedicaResponseDTO;
import tip.java.sistemacentrocrecer.mapper.CondicionMedicaMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CondicionMedicaService {
    private final CondicionMedicaRepository condicionMedicaRepository;
    private final NinioRepository ninioRepository;
    private final CondicionMedicaMapper condicionMedicaMapper;

    @Transactional(readOnly = true)
    public List<CondicionMedicaResponseDTO> listarTodos() {
        return condicionMedicaRepository.findAll()
                .stream()
                .map(condicionMedicaMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CondicionMedicaResponseDTO obtenerPorId(Integer id) {

        CondicionMedica condicionMedica = condicionMedicaRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Condición médica no encontrada"));

        return condicionMedicaMapper.toResponseDTO(condicionMedica);
    }

    @Transactional
    public CondicionMedicaResponseDTO crear(CondicionMedicaRequestDTO dto) {

        CondicionMedica condicionMedica = condicionMedicaMapper.toEntity(dto);

        Ninio ninio = ninioRepository.findById(dto.getNinioId())
                .orElseThrow(() -> new RuntimeException("Niño no encontrado"));

        condicionMedica.setNinio(ninio);

        condicionMedica = condicionMedicaRepository.save(condicionMedica);

        return condicionMedicaMapper.toResponseDTO(condicionMedica);
    }

    @Transactional
    public CondicionMedicaResponseDTO actualizar(Integer id, CondicionMedicaRequestDTO dto) {
        CondicionMedica condicionMedica = condicionMedicaRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Condición médica no encontrada"));

        Ninio ninio = ninioRepository.findById(dto.getNinioId())
                .orElseThrow(() -> new RuntimeException("Niño no encontrado"));

        condicionMedica.setCondicion(dto.getCondicion());
        condicionMedica.setObservaciones(dto.getObservacion());
        condicionMedica.setEsCronica(dto.getEsCronica());
        condicionMedica.setNinio(ninio);

        condicionMedica = condicionMedicaRepository.save(condicionMedica);

        return condicionMedicaMapper.toResponseDTO(condicionMedica);
    }

    @Transactional
    public void eliminar(Integer id) {
        CondicionMedica condicionMedica = condicionMedicaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Condición médica no encontrada"));

        condicionMedicaRepository.delete(condicionMedica);
    }
}
