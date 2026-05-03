package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

@Data
public class ReporteGrupoResponseDTO {
    private Integer id;
    private Integer reporteId;
    private String reporteTitulo;
    private Integer grupoId;
    private String grupoNombre;

}
