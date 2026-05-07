package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

@Data
public class ResponsableNinioResponseDTO {
    private Integer id;
    private NinioResponseDTO ninio_id;
    private ResponsableNinioResponseDTO responsable_id;
    private String tipo_relacion;
    private Boolean autorizado_retiro;
}
