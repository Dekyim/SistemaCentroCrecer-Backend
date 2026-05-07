package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CondicionMedicaRequestDTO {

    @NotBlank(message = "La condicion es obligatoria")
    private String condicion;

    private String observacion;

    @NotNull(message = "Debe indicar si es cronica")
    private Boolean esCronica;

    @NotNull(message = "El ninioId es obligatorio")
    private Integer ninioId;
}