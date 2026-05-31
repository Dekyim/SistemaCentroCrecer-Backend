package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AsistenciaResponseDTO {
    private Integer id;

    private LocalDate fecha;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private String observaciones;
    private Boolean activo;

    private Integer ninioId;
    private String ninioNombre;
    private String ninioCedula;
    private String ninioApellido;
    private String grupoNombre;

    private Integer funcionarioId;
    private String funcionarioNombre;
    private String funcionarioCedula;
}