package tip.java.sistemacentrocrecer.dto;

import lombok.Data;
import tip.java.sistemacentrocrecer.biz.dao.enums.EstadoLimpiezaEnum;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AgendaLimpiezaResponseDTO {

    private Integer id;
    private String descripcion;
    private String zona;
    private Integer frecuencia;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private EstadoLimpiezaEnum estado;
    private Integer funcionarioId;
    private String funcionarioNombre;
    private Integer subtipoAgendaId;
    private String subtipoAgendaNombre;
}
