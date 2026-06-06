package tip.java.sistemacentrocrecer.dto;

import lombok.Data;
import tip.java.sistemacentrocrecer.biz.dao.enums.SexoNinioEnum;

import java.time.LocalDate;
import java.util.List;

@Data
public class NinioResponseDTO {
    private Integer id;
    private String cedula;
    private String nombre;
    private String apellido;
    private SexoNinioEnum sexo;
    private String direccion;
    private String observaciones;
    private LocalDate fechaNacimiento;
    private boolean activo;
    private LocalDate fechaBaja;

    private Integer grupoId;
    private String grupoNombre;

    private GrupoResumenDTO grupo;

    private List<CondicionMedicaResponseDTO> condicionesMedicas;

    @Data
    public static class GrupoResumenDTO {
        private Integer id;
        private String nombre;
        private String rangoEdad;
        private String horaInicio;
        private String horaFin;
    }
}