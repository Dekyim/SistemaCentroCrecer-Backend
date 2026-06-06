package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RegistroCompletoRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, message = "El apellido debe tener al menos 2 caracteres")
    private String apellido;

    @NotBlank(message = "La cédula es obligatoria")
    @Pattern(regexp = "^[0-9]+$", message = "La cédula debe contener solo números")
    private String cedula;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es válido")
    private String email;

    private String telefono;

    private LocalDate fechaNacimiento;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 10, message = "La contraseña debe tener al menos 10 caracteres")
    private String contrasenia;

    @NotEmpty(message = "Debe inscribir al menos un niño")
    @Valid
    private List<NinioSolicitudDTO> ninos;


    @Data
    public static class NinioSolicitudDTO {

        @NotBlank(message = "La cédula del niño es obligatoria")
        @Pattern(regexp = "^[0-9]+$", message = "La cédula debe contener solo números")
        private String cedula;

        @NotBlank(message = "El nombre del niño es obligatorio")
        @Size(min = 2)
        private String nombre;

        @NotBlank(message = "El apellido del niño es obligatorio")
        @Size(min = 2)
        private String apellido;

        @NotBlank(message = "El sexo es obligatorio")
        private String sexo;

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        private LocalDate fechaNacimiento;

        @Size(max = 255)
        private String direccion;

        private String observaciones;

        @Valid
        private List<CondicionMedicaSolicitudDTO> condicionesMedicas;
    }

    @Data
    public static class CondicionMedicaSolicitudDTO {

        @NotBlank(message = "La condición médica es obligatoria")
        private String condicion;

        private String observacion;

        private boolean esCronica;
    }
}