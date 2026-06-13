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
import tip.java.sistemacentrocrecer.biz.dao.repositories.ResponsableNinioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ResponsableRepository;
import tip.java.sistemacentrocrecer.dto.NinioRequestDTO;
import tip.java.sistemacentrocrecer.dto.NinioResponseDTO;
import tip.java.sistemacentrocrecer.dto.NinioResponsableUpdateDTO;
import tip.java.sistemacentrocrecer.mapper.NinioMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NinioService {
    private final NinioRepository ninioRepository;
    private final GrupoRepository grupoRepository;
    private final CondicionMedicaRepository condicionMedicaRepository;
    private final ResponsableNinioRepository responsableNinioRepository;
    private final ResponsableRepository responsableRepository;
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

            ninio = ninioRepository.findById(ninio.getId()).orElseThrow(() -> new RuntimeException("Error al recuperar el niño guardado"));
        }

        return ninioMapper.toDTO(ninio);
    }

    @Transactional(readOnly = true)
    public List<NinioResponseDTO> listar() {
        // Query 1: ninios con grupo y responsables
        List<Ninio> ninios = ninioRepository.findAllWithResponsables();

        // Query 2: inicializa condiciones médicas — solo se copian las condiciones
        // al objeto original para no perder grupo, responsables ni fotoUrl
        if (!ninios.isEmpty()) {
            List<Ninio> conCondiciones = ninioRepository.findAllWithCondiciones(ninios);
            Map<Integer, List<tip.java.sistemacentrocrecer.biz.dao.entities.CondicionMedica>> condicionesMap =
                    conCondiciones.stream()
                            .collect(Collectors.toMap(
                                    Ninio::getId,
                                    n -> n.getCondiciones() != null ? n.getCondiciones() : new java.util.ArrayList<>()
                            ));
            ninios.forEach(n -> n.setCondiciones(
                    condicionesMap.getOrDefault(n.getId(), new java.util.ArrayList<>())
            ));
        }

        return ninios.stream()
                .map(ninioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NinioResponseDTO obtenerPorId(Integer id) {
        Ninio ninio = ninioRepository.findByIdWithResponsables(id).orElseThrow(() -> new RuntimeException("Niño no encontrado"));
        // Segunda query para condiciones
        ninioRepository.findByIdWithCondiciones(id).ifPresent(n -> ninio.setCondiciones(n.getCondiciones()));
        return ninioMapper.toDTO(ninio);
    }

    @Transactional
    public NinioResponseDTO actualizar(Integer id, NinioRequestDTO dto) {
        Ninio ninio = ninioRepository.findById(id).orElseThrow(() -> new RuntimeException("Niño no encontrado"));

        Grupo grupo = grupoRepository.findById(dto.getGrupo().getId()).orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

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

    @Transactional
    public NinioResponseDTO actualizarFoto(Integer id, String fotoUrl) {
        Ninio ninio = ninioRepository.findById(id).orElseThrow(() -> new RuntimeException("Niño no encontrado"));
        ninio.setFotoUrl(fotoUrl);
        return ninioMapper.toDTO(ninioRepository.save(ninio));
    }

    @Transactional(readOnly = true)
    public List<NinioResponseDTO> listarPorResponsable(Integer responsableId) {
        List<Ninio> ninios = responsableNinioRepository.findNiniosByResponsableId(responsableId);
        if (!ninios.isEmpty()) {
            responsableNinioRepository.findNiniosWithCondiciones(ninios);
        }
        return ninioMapper.toDTOList(ninios);
    }

    @Transactional
    public NinioResponseDTO actualizarPorResponsable(Integer ninioId, Integer responsableId, NinioResponsableUpdateDTO dto) {
        if (!responsableNinioRepository.existsByNinioIdAndResponsableId(ninioId, responsableId)) {
            throw new RuntimeException("No tenés permiso para modificar los datos de este niño");
        }
        Ninio ninio = ninioRepository.findById(ninioId)
                .orElseThrow(() -> new RuntimeException("Niño no encontrado"));

        ninio.setDireccion(dto.getDireccion());
        ninio.setObservaciones(dto.getObservaciones());
        ninioRepository.save(ninio);

        // Sincronizar condiciones médicas
        List<CondicionMedica> existentes = condicionMedicaRepository.findByNinioId(ninioId);

        if (dto.getCondicionesMedicas() != null) {
            // IDs que vienen en el request (las que se conservan/actualizan)
            List<Integer> idsEnviados = dto.getCondicionesMedicas().stream()
                    .filter(c -> c.getCondicionId() != null)
                    .map(NinioResponsableUpdateDTO.CondicionMedicaInlineDTO::getCondicionId)
                    .collect(Collectors.toList());

            // Eliminar las que ya no están en el request
            existentes.stream()
                    .filter(e -> !idsEnviados.contains(e.getCondicionId()))
                    .forEach(condicionMedicaRepository::delete);

            final Ninio ninioRef = ninio;
            for (NinioResponsableUpdateDTO.CondicionMedicaInlineDTO c : dto.getCondicionesMedicas()) {
                if (c.getCondicionId() != null) {
                    // Actualizar existente
                    condicionMedicaRepository.findById(c.getCondicionId()).ifPresent(cm -> {
                        cm.setCondicion(c.getCondicion());
                        cm.setObservaciones(c.getObservacion());
                        cm.setEsCronica(c.getEsCronica());
                        condicionMedicaRepository.save(cm);
                    });
                } else {
                    // Crear nueva
                    CondicionMedica nueva = CondicionMedica.builder()
                            .condicion(c.getCondicion())
                            .observaciones(c.getObservacion())
                            .esCronica(c.getEsCronica())
                            .ninio(ninioRef)
                            .build();
                    condicionMedicaRepository.save(nueva);
                }
            }
        } else {
            // Si viene null, eliminar todas
            condicionMedicaRepository.deleteAll(existentes);
        }

        // Recargar con condiciones actualizadas
        Ninio recargado = ninioRepository.findByIdWithCondiciones(ninioId)
                .orElseThrow(() -> new RuntimeException("Niño no encontrado"));
        ninioRepository.findByIdWithResponsables(ninioId).ifPresent(full -> {
            recargado.setResponsables(full.getResponsables());
            recargado.setGrupo(full.getGrupo());
        });
        return ninioMapper.toDTO(recargado);
    }
}