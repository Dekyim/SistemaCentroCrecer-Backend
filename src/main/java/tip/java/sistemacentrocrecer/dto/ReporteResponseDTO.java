package tip.java.sistemacentrocrecer.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class ReporteResponseDTO {
    private Integer id;
    private String titulo;
    private String descripcion;
    private Date fecha_generacion;
    private Boolean visto;
    private Boolean activo;
    private LocalDateTime fecha_baja;
    private FuncionarioResponseDTO funcionario;
    private List<String> grupos;
    private List<String> ninios;
    private List<DocumentoAdjuntoResponseDTO> documentos;


}
