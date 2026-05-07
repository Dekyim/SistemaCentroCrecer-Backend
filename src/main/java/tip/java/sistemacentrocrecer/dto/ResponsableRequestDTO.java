package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponsableRequestDTO {

    @NotBlank(message = "La cedula no puede estar vacia")
    @Pattern(regexp = "^[0-9]+$")
    private String cedula;

    @NotBlank(message = "El nombre no puede estar vacio")
    @Size(min = 2, max = 100)
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacio")
    @Size(min = 2, max = 100)
    private String apellido;

    private LocalDate fecha_nacimiento;

    @Pattern(regexp = "^[0-9+\\-\\s()]*$")
    private String telefono;

    @Email
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 10, message = "La contraseña debe tener al menos 10 caracteres")
    private String contrasenia;
}