package tip.java.sistemacentrocrecer.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class DetalleAgendaResponseDTO {
    private Integer id;
    private String descripcionEspecifica;
    private Boolean requiereParticipantes;
    private Boolean activo;
    private LocalDateTime fechaBaja;
    private Integer agendaId;
    private Integer subtipoAgendaId;
    private String subtipoAgendaNombre;

}