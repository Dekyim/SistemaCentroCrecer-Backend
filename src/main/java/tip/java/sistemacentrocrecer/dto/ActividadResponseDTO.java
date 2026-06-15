package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class ActividadResponseDTO {
    private Integer id;
    private String nombre;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private LocalTime horaInicio;
    private LocalTime horaSalida;
    private String descripcion;
    private Boolean activo;
    private LocalDateTime fechaBaja;
    private String lugar;
    private List<NinioResponseDTO> ninios;
    private List<EmpresaExternaResponseDTO> empresasExternas;
}