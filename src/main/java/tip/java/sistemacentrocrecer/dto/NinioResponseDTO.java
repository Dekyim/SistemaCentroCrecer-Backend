package tip.java.sistemacentrocrecer.dto;

import lombok.Data;
import tip.java.sistemacentrocrecer.biz.dao.enums.SexoNinioEnum;
import java.time.LocalDate;
import java.util.List;

@Data
public class NinioResponseDTO {
    private Integer id;
    private String cedula;
    private String  nombre;
    private String  apellido;
    private SexoNinioEnum sexo;
    private String direccion;
    private String observaciones;
    private LocalDate fecha_nacimiento;
    private boolean activo;
    private LocalDate fecha_baja;
    private GrupoResponseDTO grupo_id;
}
