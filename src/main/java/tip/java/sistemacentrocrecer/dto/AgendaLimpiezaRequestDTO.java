package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

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

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    private LocalTime horaFin;

    @NotNull(message = "El funcionario es obligatorio")
    private Integer funcionarioId;

    @NotNull(message = "El subtipo de agenda es obligatorio")
    private Integer subtipoAgendaId;
}
