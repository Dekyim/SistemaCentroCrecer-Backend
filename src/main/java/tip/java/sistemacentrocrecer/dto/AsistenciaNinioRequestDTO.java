package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AsistenciaNinioRequestDTO {

    @NotNull(message = "La fecha no puede ser nula")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fecha;

    @NotNull(message = "La hora de entrada no puede ser nula")
    private LocalTime horaEntrada;

    private LocalTime horaSalida;

    @Size(max = 500, message = "Las observaciones no pueden superar los 500 caracteres")
    private String observaciones;

    @NotNull(message = "El id del niño no puede ser nulo")
    private Integer ninioId;

    private Integer actividadId;
}