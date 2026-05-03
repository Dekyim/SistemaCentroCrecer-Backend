package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class GrupoResponseDTO {
    @NotBlank(message = "El nombre del grupo no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @Pattern(regexp = "^\\d+-\\d+$", message = "El rango de edad debe tener el formato '3-5'")
    private String rangoEdad;

    @NotNull(message = "La hora de inicio no puede ser nula")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin no puede ser nula")
    private LocalTime horaFin;

    //IDs de funcionarios a asignar al grupo
    private List<Integer> funcionariosIds;

    //IDs de niños a asignar al grupo
    private List<Integer> niniosIds;

}
