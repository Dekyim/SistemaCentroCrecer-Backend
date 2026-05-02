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
    private LocalDateTime fecha_baja;
    private String rango_edad;
    private LocalTime hora_inicio;
    private LocalTime hora_fin;
    private Integer cantidad_ninios;
    private List<FuncionarioResponseDTO> funcionarios;
    private List<NinioResponseDTO> ninios;

}
