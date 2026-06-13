package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class NinioResponsableUpdateDTO {

    @Size(max = 255)
    private String direccion;

    private String observaciones;

    @Valid
    private List<CondicionMedicaInlineDTO> condicionesMedicas;

    @Data
    public static class CondicionMedicaInlineDTO {
        private Integer condicionId;

        @NotBlank(message = "La condición es obligatoria")
        private String condicion;

        private String observacion;

        @jakarta.validation.constraints.NotNull(message = "Debe indicar si es crónica")
        private Boolean esCronica;
    }
}