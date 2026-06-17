package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class ActividadRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @NotNull(message = "fechaDesde es obligatoria")
    private LocalDate fechaDesde;

    @NotNull(message = "fechaHasta es obligatoria")
    private LocalDate fechaHasta;

    @NotNull(message = "horaInicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "horaSalida es obligatoria")
    private LocalTime horaSalida;

    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
    private String descripcion;

    @NotBlank(message = "El lugar es obligatorio")
    @Size(max = 200, message = "El lugar no puede superar los 200 caracteres")
    private String lugar;

    @Min(value = 0, message = "El límite de días no puede ser negativo")
    private Integer diasLimiteModificacion;

    private List<Integer> niniosIds;

    private List<Integer> permisosIds;

    private List<Integer> empresasExternasIds;
}