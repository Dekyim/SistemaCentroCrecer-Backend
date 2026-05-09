package tip.java.sistemacentrocrecer.biz.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tip.java.sistemacentrocrecer.biz.dao.entities.Funcionario;
import tip.java.sistemacentrocrecer.biz.dao.entities.Grupo;
import tip.java.sistemacentrocrecer.biz.dao.entities.Ninio;
import tip.java.sistemacentrocrecer.biz.dao.repositories.FuncionarioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.GrupoRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.NinioRepository;
import tip.java.sistemacentrocrecer.dto.GrupoRequestDTO;
import tip.java.sistemacentrocrecer.dto.GrupoResponseDTO;
import tip.java.sistemacentrocrecer.exceptions.BusinessException;
import tip.java.sistemacentrocrecer.exceptions.ResourceNotFoundException;
import tip.java.sistemacentrocrecer.mapper.GrupoMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class GrupoService {
    private final GrupoRepository grupoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final NinioRepository ninioRepository;
    private final GrupoMapper grupoMapper;

    public List<GrupoResponseDTO> listarTodos() {
        return grupoRepository.findAll().stream()
                .map(grupoMapper::toResponseDTO)
                .toList();
    }

    public GrupoResponseDTO buscarPorId(Integer id) {
        Grupo grupo = grupoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado"));
        return grupoMapper.toResponseDTO(grupo);
    }

    public List<GrupoResponseDTO> listarGruposActivos(){
        return grupoRepository.findByActivoTrue().stream()
                .map(grupoMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public GrupoResponseDTO crear(GrupoRequestDTO dto) {

        if (dto.getHoraFin().isBefore(dto.getHoraInicio())) {
            throw new BusinessException("La hora de fin no puede ser menor que la de inicio");
        }

        Grupo grupo = grupoMapper.toEntity(dto);

        //asignar funcionarios
        if (dto.getFuncionariosIds() != null) {
            List<Funcionario> funcionarios = funcionarioRepository.findAllById(dto.getFuncionariosIds());
            grupo.setFuncionarios(funcionarios);
        }

        //asignar niños
        if (dto.getNiniosIds() != null) {
            List<Ninio> ninios = ninioRepository.findAllById(dto.getNiniosIds());
            grupo.setNinios(ninios);

            //importante: setear relación inversa
            ninios.forEach(n -> n.setGrupo(grupo));
        }

        return grupoMapper.toResponseDTO(grupoRepository.save(grupo));
    }

    @Transactional
    public GrupoResponseDTO actualizar(Integer id, GrupoRequestDTO dto) {

        Grupo grupo = grupoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", id));

        if (dto.getHoraFin().isBefore(dto.getHoraInicio())) {
            throw new BusinessException("La hora de fin no puede ser menor que la de inicio");
        }

        grupo.setNombre(dto.getNombre());
        grupo.setHoraInicio(dto.getHoraInicio());
        grupo.setHoraFin(dto.getHoraFin());
        grupo.setRangoEdad(dto.getRangoEdad());


        if (dto.getFuncionariosIds() != null) {
            List<Funcionario> funcionarios = funcionarioRepository.findAllById(dto.getFuncionariosIds());
            grupo.setFuncionarios(funcionarios);
        }


        if (dto.getNiniosIds() != null) {

            if (grupo.getNinios() != null) {
                grupo.getNinios().forEach(n -> n.setGrupo(null));
            }

            List<Ninio> ninios = ninioRepository.findAllById(dto.getNiniosIds());

            grupo.setNinios(ninios);
            ninios.forEach(n -> n.setGrupo(grupo));
        }

        return grupoMapper.toResponseDTO(grupoRepository.save(grupo));
    }

    @Transactional
    public void darDeBaja(Integer id) {
        Grupo g = grupoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", id));

        if (!g.isActivo()) {
            throw new BusinessException("El grupo ya está inactivo");
        }

        g.setActivo(false);
        g.setFechaBaja(LocalDateTime.now());

        grupoRepository.save(g);
    }
}
