package tip.java.sistemacentrocrecer.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import tip.java.sistemacentrocrecer.biz.dao.enums.SexoNinioEnum;

import java.time.LocalDate;

@Data
public class NinioFilterRequestDTO {

    private String nombre;

    private String apellido;

    private String cedula;

    private Integer grupoId;

    private SexoNinioEnum sexo;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaNacimiento;

    private String direccion;

    private Boolean activo;

    private String enfermedad;
}
