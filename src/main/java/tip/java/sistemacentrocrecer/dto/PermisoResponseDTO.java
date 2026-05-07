package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PermisoResponseDTO {
    private Integer id;
    private ActividadResponseDTO actividad_id;
    private String ninio_cedula;
    private Boolean activo;
    private Date fecha_baja;
    private Boolean autorizado;
}
