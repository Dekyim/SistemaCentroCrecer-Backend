package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ResponsableResponseDTO {
    private Integer id;
    private String cedula;
    private String nombre;
    private String apellido;
    private LocalDate fecha_nacimiento;
    private String telefono;
    private Boolean activo;
    private LocalDateTime fecha_baja;
    private String email;
}
