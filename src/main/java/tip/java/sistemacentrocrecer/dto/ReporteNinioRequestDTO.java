package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

@Data
public class ReporteNinioRequestDTO {
    private Integer id;
    private Integer reporteId;
    private String reporteTitulo;
    private Integer ninioId;
    private String ninioNombre;

}
