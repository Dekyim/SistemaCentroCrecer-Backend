package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class ActividadResponseDTO {
    private Integer id;
    private String nombre;
    private LocalDate fecha_desde;
    private LocalDate fecha_hasta;
    private LocalTime hora_inicio;
    private LocalTime hora_salida;
    private String descripcion;
    private Boolean activo;
    private LocalDateTime fecha_baja;
    private String lugar;
}