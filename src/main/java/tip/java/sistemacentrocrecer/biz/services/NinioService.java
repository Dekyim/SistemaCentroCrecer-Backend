package tip.java.sistemacentrocrecer.biz.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tip.java.sistemacentrocrecer.biz.dao.entities.CondicionMedica;
import tip.java.sistemacentrocrecer.biz.dao.entities.Grupo;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;
import tip.java.sistemacentrocrecer.biz.dao.repositories.CondicionMedicaRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.GrupoRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.NinioRepository;
import tip.java.sistemacentrocrecer.dto.NinioRequestDTO;
import tip.java.sistemacentrocrecer.dto.NinioResponseDTO;
import tip.java.sistemacentrocrecer.mapper.NinioMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NinioService {
    private final NinioRepository ninioRepository;
    private final GrupoRepository grupoRepository;
    private final CondicionMedicaRepository condicionMedicaRepository;
    private final NinioMapper ninioMapper;

    @Transactional
    public NinioResponseDTO crear(NinioRequestDTO dto) {

        if (ninioRepository.findByCedula(dto.getCedula()).isPresent()) {
            throw new RuntimeException("Ya existe un niño con esa cedula");
        }

        Grupo grupo = grupoRepository.findById(dto.getGrupo().getId())
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        if (!grupo.isActivo()) {
            throw new RuntimeException("No se puede registrar un niño en un grupo inactivo");
        }

        Ninio ninio = ninioMapper.toEntity(dto, grupo);
        ninio = ninioRepository.save(ninio);

        if (dto.getCondicionesMedicas() != null && !dto.getCondicionesMedicas().isEmpty()) {
            final Ninio ninioGuardado = ninio;
            List<CondicionMedica> condiciones = dto.getCondicionesMedicas().stream()
                    .map(c -> CondicionMedica.builder()
                            .condicion(c.getCondicion())
                            .observaciones(c.getObservacion())
                            .esCronica(c.getEsCronica())
                            .ninio(ninioGuardado)
                            .build())
                    .collect(Collectors.toList());

            condicionMedicaRepository.saveAll(condiciones);

            ninio = ninioRepository.findById(ninio.getId())
                    .orElseThrow(() -> new RuntimeException("Error al recuperar el niño guardado"));
        }

        return ninioMapper.toDTO(ninio);
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

    @Transactional
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