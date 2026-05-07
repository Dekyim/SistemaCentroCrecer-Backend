package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

@Data
public class CondicionMedicaResponseDTO {
    private Integer condicionId;
    private String condicion;
    private String observacion;
    private Boolean esCronica;
}
