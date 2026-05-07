package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PermisoRequestDTO {

    @NotNull(message = "La actividadId es obligatoria")
    private Integer actividad_id;

    @NotBlank(message = "La cedula del ninio es obligatoria")
    private String ninio_cedula;

    @NotNull(message = "Debe indicar si esta autorizado")
    private Boolean autorizado;
}