package tip.java.sistemacentrocrecer.dto;

import lombok.Data;
import tip.java.sistemacentrocrecer.biz.dao.enums.TipoDiaNoLaborableEnum;

import java.time.LocalDate;

@Data
public class DiaNoLaborableResponseDTO {
    private Integer id;
    private LocalDate fecha;
    private String motivo;
    private TipoDiaNoLaborableEnum tipo;
    private Boolean activo;
    private Integer creadoPorId;
    private String creadoPorNombre;
}
