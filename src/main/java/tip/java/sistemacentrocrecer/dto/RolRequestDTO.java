package tip.java.sistemacentrocrecer.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RolRequestDTO {
    private Integer id;
    private String nombre;
    private boolean activo;
    private LocalDate fechaBaja;
    private Integer padreId;
    private String padreNombre;
}
