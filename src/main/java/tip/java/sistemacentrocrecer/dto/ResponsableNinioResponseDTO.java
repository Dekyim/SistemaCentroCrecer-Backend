package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

@Data
public class ResponsableNinioResponseDTO {
    private Integer id;
    private NinioResponseDTO ninio;
    private ResponsableResponseDTO responsable;
    private String tipoRelacion;
    private Boolean autorizadoRetiro;
}
