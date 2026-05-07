package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TurnoResponseDTO {
    private Integer id;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    private Integer funcionarioId;
    private String funcionarioNombre;

}