package tip.java.sistemacentrocrecer.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificacionResponseDTO {
    private Integer id;
    private String mensaje;
    private Boolean leida;
    private LocalDateTime fechaCreacion;
    private Integer reporteId;
    private String reporteTitulo;
}