package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FrecuenciaAsistenciaResponseDTO {
    private Integer ninioId;
    private String ninioNombre;
    private String ninioApellido;
    private String ninioCedula;
    private String grupoNombre;

    private LocalDate desde;
    private LocalDate hasta;

    private long diasPresente;
    private long diasAusente;
    private long totalDiasHabiles;
    private double porcentajeAsistencia;
    private double porcentajeInasistencia;
}