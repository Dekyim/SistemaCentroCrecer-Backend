package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import tip.java.sistemacentrocrecer.biz.dao.enums.EstadoInscripcionEnum;

import java.time.LocalDate;

@Data
public class InscripcionRequestDTO {

    @NotNull(message = "El ninioId es obligatorio")
    private Integer ninioId;

    @NotNull(message = "El responsableId es obligatorio")
    private Integer responsableId;

    @NotNull(message = "La fechaInscripcion es obligatoria")
    private LocalDate fechaInscripcion;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @NotNull(message = "El estadoInscripcion es obligatorio")
    private EstadoInscripcionEnum estadoInscripcion;

    private String observaciones;
}