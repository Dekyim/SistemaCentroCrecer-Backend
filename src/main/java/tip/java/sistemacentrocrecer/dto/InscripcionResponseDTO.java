package tip.java.sistemacentrocrecer.dto;
import tip.java.sistemacentrocrecer.biz.dao.enums.EstadoInscripcionEnum;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class InscripcionResponseDTO {
    private Integer id;
    private NinioResponseDTO ninio;
    private LocalDate fechaInscripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoInscripcionEnum estadoInscripcion;
    private String motivoBaja;
    private String observaciones;

    private List<ResponsableResponseDTO> responsables;
}
