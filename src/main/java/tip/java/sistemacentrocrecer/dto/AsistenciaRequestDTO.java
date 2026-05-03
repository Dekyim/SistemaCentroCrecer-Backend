package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AsistenciaRequestDTO {
    private Integer id;

    private LocalDate fecha;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private String observaciones;

    private Integer ninioId;
    private String ninioNombre;

    private Integer funcionarioId;
    private String funcionarioNombre;
}