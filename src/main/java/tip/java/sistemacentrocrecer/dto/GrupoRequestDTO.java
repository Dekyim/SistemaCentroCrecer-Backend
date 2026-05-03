package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class GrupoRequestDTO {
    private Integer id;
    private String nombre;
    private boolean activo;
    private LocalDateTime fechaBaja;
    private String rangoEdad;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer cantidadNinios;
    private List<FuncionarioRequestDTO> funcionarios;
    private List<NinioResponseDTO> ninios;

}
