package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

@Data
public class ReporteNinioResponseDTO {
    private Integer id;
    private Integer reporteId;
    private String reporteTitulo;
    private Integer ninioId;
    private String ninioNombre;
    private String ninioApellido;
}