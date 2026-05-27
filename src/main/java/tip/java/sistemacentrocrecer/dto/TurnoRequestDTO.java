package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class TurnoRequestDTO {

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    @NotNull(message = "El funcionario es obligatorio")
    private Integer funcionarioId;

    @Size(min = 1, message = "Debe seleccionar al menos un día")
    private List<DayOfWeek> dias = new ArrayList<>();
}