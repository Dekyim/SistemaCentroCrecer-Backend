package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TipoAgendaRequestDTO {
    @NotBlank(message = "El tipo no puede estar vacío")
    @Size(min = 2, max = 50, message = "El tipo debe tener entre 2 y 50 caracteres")
    private String tipo;

    @NotNull(message = "La agenda es obligatoria")
    private Integer agendaId;
}
