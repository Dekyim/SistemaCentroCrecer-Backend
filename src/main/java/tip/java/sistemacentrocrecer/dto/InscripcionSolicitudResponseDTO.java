package tip.java.sistemacentrocrecer.dto;

import lombok.Data;
import tip.java.sistemacentrocrecer.biz.dao.enums.EstadoInscripcionEnum;

import java.time.LocalDate;
import java.util.List;

@Data
public class InscripcionSolicitudResponseDTO {

    private Integer id;
    private LocalDate fechaInscripcion;
    private EstadoInscripcionEnum estadoInscripcion;
    private String motivoBaja;
    private String observaciones;

    private Integer responsableId;
    private String responsableNombre;
    private String responsableCedula;
    private String responsableEmail;
    private String responsableTelefono;

    private Integer ninioId;
    private String ninioNombre;
    private String ninioApellido;
    private String ninioCedula;
    private LocalDate ninioFechaNacimiento;
    private String ninioSexo;
    private String ninioObservaciones;
    private String ninioDireccion;
    private List<CondicionMedicaResponseDTO> condicionesMedicas;

    private Integer grupoId;
    private String grupoNombre;
}