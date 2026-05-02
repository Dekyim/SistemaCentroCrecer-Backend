package tip.java.sistemacentrocrecer.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RolResponseDTO {
    private Integer id;
    private String nombre;
    private boolean activo;
    private LocalDate fecha_baja;
    private Integer padre_id;
    private String padre_nombre;
}
