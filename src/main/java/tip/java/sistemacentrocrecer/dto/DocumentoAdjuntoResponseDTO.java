package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DocumentoAdjuntoResponseDTO {
    private Integer id;
    private String nombreArchivo;
    private String tipoArchivo;
    private String url;
    private Date fechaSubida;
    private Integer reporteId;
    private String reporteTitulo;
}
