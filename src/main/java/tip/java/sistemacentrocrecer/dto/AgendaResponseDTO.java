package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AgendaResponseDTO {
    private Integer id;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String descripcion;

    private Integer funcionarioId;
    private String funcionarioNombre;

    private Integer tipoId;
    private String tipoNombre;
}