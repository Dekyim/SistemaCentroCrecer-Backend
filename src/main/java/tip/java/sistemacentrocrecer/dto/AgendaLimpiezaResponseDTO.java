package tip.java.sistemacentrocrecer.dto;

import lombok.Data;
import tip.java.sistemacentrocrecer.biz.dao.enums.EstadoLimpiezaEnum;


@Data
public class AgendaLimpiezaResponseDTO {

    private Integer id;
    private String descripcion;
    private String zona;
    private Integer frecuencia;
    private EstadoLimpiezaEnum estado;
    private Integer agendaId;
    private Integer subtipoAgendaId;
    private String subtipoAgendaNombre;

}