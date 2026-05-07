package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class SubtipoAgendaResponseDTO {
    private Integer subtipoId;
    private String subtipo;
    private Integer detalleAgendaId;
    private Integer agendaLimpiezaId;
}