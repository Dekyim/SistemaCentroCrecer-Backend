package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AsistenciaRequestDTO {
    private Integer id;
    private LocalDate fecha;
    private LocalTime hora_entrada;
    private LocalTime hora_salida;
    private String observaciones;
    private Boolean activo;
    private Integer ninioId;
    private String ninio_nombre;
    private String ninio_cedula;
    private Integer funcionario_id;
    private String funcionario_nombre;
    private String funcionario_cedula;

}
