package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AgendaLimpiezaRequestDTO {


    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcion;

    @NotBlank(message = "La zona no puede estar vacía")
    @Size(max = 40, message = "La zona no puede superar los 40 caracteres")
    private String zona;

    @NotNull(message = "La frecuencia es obligatoria")
    private Integer frecuencia;

    @NotNull(message = "La agenda es obligatoria")
    private Integer agendaId;

    @NotNull(message = "El subtipo de agenda es obligatorio")
    private Integer subtipoAgendaId;

}
