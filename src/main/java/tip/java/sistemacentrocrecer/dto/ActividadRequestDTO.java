package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ActividadRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "fechaDesde es obligatoria")
    private LocalDate fecha_desde;

    @NotNull(message = "fechaHasta es obligatoria")
    private LocalDate fecha_hasta;

    @NotNull(message = "horaInicio es obligatoria")
    private LocalTime hora_inicio;

    @NotNull(message = "horaSalida es obligatoria")
    private LocalTime hora_salida;

    private String descripcion;

    @NotBlank(message = "El lugar es obligatorio")
    private String lugar;
}