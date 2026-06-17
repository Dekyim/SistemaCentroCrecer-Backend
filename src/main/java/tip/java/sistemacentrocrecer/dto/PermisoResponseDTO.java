package tip.java.sistemacentrocrecer.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PermisoResponseDTO {
    private Integer id;
    private ActividadResponseDTO actividad;
    private String ninioCedula;
    private NinioResponseDTO ninio;
    private Boolean activo;
    private LocalDateTime fechaBaja;
    private Boolean autorizado;
    private Boolean respondido;
}
