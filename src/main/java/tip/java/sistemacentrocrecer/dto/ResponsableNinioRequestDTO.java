package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ResponsableNinioRequestDTO {

    @NotNull(message = "El ninio es obligatorio")
    private Integer ninioId;

    @NotNull(message = "El responsable es obligatorio")
    private Integer responsableId;

    @NotBlank(message = "El tipo de relación es obligatorio")
    private String tipoRelacion;

    @NotNull(message = "Debe indicar si está autorizado")
    private Boolean autorizadoRetiro;
}