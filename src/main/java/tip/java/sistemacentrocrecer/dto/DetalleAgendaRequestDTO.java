package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DetalleAgendaRequestDTO {

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcionEspecifica;

    @NotNull(message = "Debe indicar si requiere participantes o no")
    private Boolean requiereParticipantes;

    @NotNull(message = "La agenda es obligatoria")
    private Integer agendaId;

    @NotNull(message = "El subtipo de agenda es obligatorio")
    private Integer subtipoAgendaId;
}
