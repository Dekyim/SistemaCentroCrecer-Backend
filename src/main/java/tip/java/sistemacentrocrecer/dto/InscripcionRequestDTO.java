package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import tip.java.sistemacentrocrecer.biz.dao.enums.EstadoInscripcionEnum;

import java.time.LocalDate;

@Data
public class InscripcionRequestDTO {

    @NotNull(message = "El ninioId es obligatorio")
    private NinioResponseDTO ninio_id;

    @NotNull(message = "El responsableId es obligatorio")
    private ResponsableNinioResponseDTO responsable_id;

    @NotNull(message = "La fechaInscripcion es obligatoria")
    private LocalDate fecha_inscripcion;

    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;

    @NotNull(message = "El estadoInscripcion es obligatorio")
    private EstadoInscripcionEnum estado_inscripcion;

    private String observaciones;
}