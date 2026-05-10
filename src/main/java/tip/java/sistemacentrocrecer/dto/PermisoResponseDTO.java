package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class PermisoResponseDTO {
    private Integer id;
    private ActividadResponseDTO actividad;
    private String ninioCedula;
    private Boolean activo;
    private LocalDateTime fechaBaja;
    private Boolean autorizado;
}
