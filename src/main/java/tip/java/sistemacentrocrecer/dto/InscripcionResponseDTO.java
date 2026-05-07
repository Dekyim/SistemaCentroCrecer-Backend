package tip.java.sistemacentrocrecer.dto;
import tip.java.sistemacentrocrecer.biz.dao.enums.EstadoInscripcionEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InscripcionResponseDTO {
    private Integer id;
    private NinioResponseDTO ninio_id;
    private LocalDate fecha_inscripcion;
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private EstadoInscripcionEnum estado_inscripcion;
    private String motivo_baja;
    private String observaciones;
    private ResponsableNinioResponseDTO responsable_id;
}
