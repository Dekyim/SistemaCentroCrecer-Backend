package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

@Data
public class ReporteGrupoResponseDTO {
    private Integer id;
    private Integer reporte_id;
    private String reporte_titulo;
    private Integer grupo_id;
    private String grupo_nombre;

}
