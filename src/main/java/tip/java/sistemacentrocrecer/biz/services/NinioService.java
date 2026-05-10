package tip.java.sistemacentrocrecer.biz.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.entities.Grupo;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;
import tip.java.sistemacentrocrecer.biz.dao.repositories.GrupoRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.NinioRepository;
import tip.java.sistemacentrocrecer.dto.NinioRequestDTO;
import tip.java.sistemacentrocrecer.dto.NinioResponseDTO;
import tip.java.sistemacentrocrecer.mapper.NinioMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NinioService {
    private final NinioRepository ninioRepository;
    private final GrupoRepository grupoRepository;
    private final NinioMapper ninioMapper;

    public NinioResponseDTO crear(NinioRequestDTO dto) {

        if (ninioRepository.findByCedula(dto.getCedula()).isPresent()) {
            throw new RuntimeException("Ya existe un niño con esa cedula");
        }

        Grupo grupo = grupoRepository.findById(dto.getGrupo().getId())
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        Ninio ninio = ninioMapper.toEntity(dto, grupo);

        return ninioMapper.toDTO(
                ninioRepository.save(ninio)
        );
    }

    public List<NinioResponseDTO> listar() {
        return ninioRepository.findAll()
                .stream()
                .map(ninioMapper::toDTO)
                .collect(Collectors.toList());
    }

    public NinioResponseDTO obtenerPorId(Integer id) {
        Ninio ninio = ninioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Niño no encontrado"));

        return ninioMapper.toDTO(ninio);
    }

    public NinioResponseDTO actualizar(Integer id, NinioRequestDTO dto) {
        Ninio ninio = ninioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Niño no encontrado"));

        Grupo grupo = grupoRepository.findById(dto.getGrupo().getId())
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        ninio.setCedula(dto.getCedula());
        ninio.setNombre(dto.getNombre());
        ninio.setApellido(dto.getApellido());
        ninio.setSexo(dto.getSexo());
        ninio.setDireccion(dto.getDireccion());
        ninio.setObservaciones(dto.getObservaciones());
        ninio.setFechaNacimiento(java.sql.Date.valueOf(dto.getFechaNacimiento()));
        ninio.setGrupo(grupo);

        return ninioMapper.toDTO(ninioRepository.save(ninio));
    }

    public void eliminar(Integer id) {

        Ninio ninio = ninioRepository.findById(id).orElseThrow(() -> new RuntimeException("Niño no encontrado"));

        ninio.setActivo(false);
        ninio.setFechaBaja(java.time.LocalDateTime.now());

        ninioRepository.save(ninio);
    }
}
