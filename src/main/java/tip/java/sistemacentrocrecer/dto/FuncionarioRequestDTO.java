package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class FuncionarioRequestDTO {
    private Integer id;
    private String cedula;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private LocalDate fechaNacimiento;
    private boolean activo;
    private LocalDate fechaBaja;
    private RolRequestDTO rol;
    private List<String> grupos;

}
