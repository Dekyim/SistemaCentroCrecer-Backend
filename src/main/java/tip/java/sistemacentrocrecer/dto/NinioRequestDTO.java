package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import tip.java.sistemacentrocrecer.biz.dao.enums.SexoNinioEnum;

import java.time.LocalDate;
import java.util.List;

@Data
public class NinioRequestDTO {

    @NotBlank(message = "La cedula no puede estar vacia")
    @Size(max = 8, message = "La cedula debe tener maximo 8 caracteres")
    @Pattern(regexp = "^[0-9]+$", message = "La cedula solo debe contener numeros")
    private String cedula;

    @NotBlank(message = "El nombre no puede estar vacio")
    @Size(min = 2, max = 100)
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacio")
    @Size(min = 2, max = 100)
    private String apellido;

    @NotNull(message = "El sexo no puede ser nulo")
    private SexoNinioEnum sexo;

    @Size(max = 255)
    private String direccion;

    private String observaciones;

    @NotNull
    private GrupoResponseDTO grupo;

    @NotNull(message = "La fechaNacimiento no puede ser nula")
    private LocalDate fechaNacimiento;


    @Valid
    private List<CondicionMedicaInlineDTO> condicionesMedicas;

    @Data
    public static class CondicionMedicaInlineDTO {

        @NotBlank(message = "La condicion es obligatoria")
        private String condicion;

        private String observacion;

        @NotNull(message = "Debe indicar si es cronica")
        private Boolean esCronica;
    }
}