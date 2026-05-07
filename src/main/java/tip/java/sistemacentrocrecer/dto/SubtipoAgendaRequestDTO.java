package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class SubtipoAgendaRequestDTO {

    @NotBlank(message = "El subtipo no puede estar vacío")
    @Size(min = 2, max = 50, message = "El subtipo debe tener entre 2 y 50 caracteres")
    private String subtipo;

    @NotNull(message = "El detalleAgenda es obligatorio")
    private Integer detalleAgendaId;

    @NotNull(message = "La agendaLimpieza es obligatoria")
    private Integer agendaLimpiezaId;

}
