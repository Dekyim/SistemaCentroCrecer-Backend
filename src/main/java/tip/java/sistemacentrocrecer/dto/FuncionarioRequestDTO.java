package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FuncionarioRequestDTO {
    @NotBlank(message = "La cédula no puede estar vacía")
    @Size(max = 8, message = "La cédula debe tener entre 8 caracteres")
    @Pattern(regexp = "^[0-9]+$", message = "La cédula solo debe contener números")
    private String cedula;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    private String apellido;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    private String email;

    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "El teléfono solo puede contener números, +, -, espacios y paréntesis")
    private String telefono;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 10, message = "La contraseña debe tener al menos 10 caracteres")
    private String contrasenia;

    private LocalDate fechaNacimiento;

    @NotNull(message = "El id del rol no puede ser nulo")
    private Integer rolId;

}
