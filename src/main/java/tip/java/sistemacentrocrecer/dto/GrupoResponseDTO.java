package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class GrupoResponseDTO {
    private Integer id;
    private String nombre;
    private boolean activo;
    private LocalDateTime fechaBaja;
    private String rangoEdad;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer cantidadNinios;
    private List<FuncionarioResponseDTO> funcionarios;
    private List<NinioResponseDTO> ninios;

}
